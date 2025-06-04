package com.sortingvisualizer;

import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class SortingVisualizerGUI extends Application {
    
    private SortingBusinessLogic businessLogic;
    private Map<String, Pane> visualPanes;
    private Map<String, Label> statusLabels;
    private Map<String, Label> timeLabels;
    private Map<String, Long> finalTimes;
    private Button startButton;
    private Button generateButton;
    private ComboBox<Integer> sizeSelector;
    private CheckBox blinkCheckBox;
    private GridPane chartsGrid;
    private StackPane root;
    
    private boolean isSortedState = false;
    private boolean blinkBars = false; // se true, le barre lampeggiano
    
    private final Color[] algorithmColors = {
        Color.web("#4CAF50"), Color.web("#2196F3"), Color.web("#FF9800"),
        Color.web("#9C27B0"), Color.web("#E91E63"), Color.web("#009688"),
        Color.web("#FFEB3B"), Color.web("#3F51B5"), Color.web("#607D8B")
    };
    
    @Override
    public void start(Stage primaryStage) {
        businessLogic = new SortingBusinessLogic(12);
        visualPanes = new HashMap<>();
        statusLabels = new HashMap<>();
        timeLabels = new HashMap<>();
        finalTimes = new HashMap<>();
        
        root = new StackPane();
        root.setCache(true);
        root.setCacheHint(CacheHint.SPEED);
        
        Pane animatedBackground = createAnimatedBackground();
        root.getChildren().add(animatedBackground);
        
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.getStyleClass().add("main-container");
        
        Label mainTitle = new Label("🌌 Sorting Visualizer");
        mainTitle.getStyleClass().add("main-title");
        
        HBox controlPanel = createControlPanel();
        
        chartsGrid = createChartsGrid();
        ScrollPane scrollPane = new ScrollPane(chartsGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStyleClass().add("transparent-scroll");
        
        mainContainer.getChildren().addAll(mainTitle, controlPanel, scrollPane);
        root.getChildren().add(mainContainer);
        
        Pane particlesLayer = createParticleLayer();
        root.getChildren().add(particlesLayer);
        
        Scene scene = new Scene(root, 1400, 900);
        scene.setFill(Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setTitle("Sorting Algorithms Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        generateNewDataset();
        
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            playExitAnimation(mainContainer);
        });
    }
    
    private Pane createAnimatedBackground() {
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(root.widthProperty());
        bg.heightProperty().bind(root.heightProperty());
        
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#0f2027")), new Stop(1, Color.web("#203a43"))
        };
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops
        );
        bg.setFill(gradient);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(bg.fillProperty(),
                    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0f2027")), new Stop(1, Color.web("#203a43"))
                    )
                )
            ),
            new KeyFrame(Duration.seconds(10),
                new KeyValue(bg.fillProperty(),
                    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#203a43")), new Stop(1, Color.web("#2c5364"))
                    )
                )
            ),
            new KeyFrame(Duration.seconds(20),
                new KeyValue(bg.fillProperty(),
                    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#2c5364")), new Stop(1, Color.web("#0f2027"))
                    )
                )
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
        
        return new Pane(bg);
    }
    
    private Pane createParticleLayer() {
        Pane layer = new Pane();
        layer.setMouseTransparent(true);
        layer.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        Timeline particleTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> {
            Circle c = new Circle(2 + Math.random() * 3, Color.web("rgba(255,255,255,0.3)"));
            c.setMouseTransparent(true);
            c.setTranslateX(Math.random() * root.getWidth());
            c.setTranslateY(root.getHeight() + 10);
            layer.getChildren().add(c);
            
            TranslateTransition tt = new TranslateTransition(Duration.seconds(6 + Math.random() * 4), c);
            tt.setByY(-root.getHeight() - 20);
            
            FadeTransition ft = new FadeTransition(Duration.seconds(6 + Math.random() * 4), c);
            ft.setFromValue(0.7);
            ft.setToValue(0.0);
            
            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(ev -> layer.getChildren().remove(c));
            pt.play();
        }));
        particleTimeline.setCycleCount(Animation.INDEFINITE);
        particleTimeline.play();
        
        return layer;
    }
    
    private HBox createControlPanel() {
        HBox panel = new HBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.getStyleClass().add("control-panel");
        
        startButton = new Button("🚀 Avvia Ordinamento");
        startButton.getStyleClass().add("start-button");
        applyButtonAnimations(startButton, true);
        startButton.setOnAction(e -> startSorting());
        
        generateButton = new Button("🎲 Nuovo Elenco Casuale");
        generateButton.getStyleClass().add("generate-button");
        applyButtonAnimations(generateButton, false);
        generateButton.setOnAction(e -> generateNewDataset());
        
        Label sizeLabel = new Label("Numero elementi:");
        sizeLabel.getStyleClass().add("instructions");
        
        sizeSelector = new ComboBox<>();
        sizeSelector.getItems().addAll(12, 20, 50, 100);
        sizeSelector.setValue(12);
        applyComboBoxAnimations(sizeSelector);
        sizeSelector.setOnAction(e -> {
            businessLogic.setArraySize(sizeSelector.getValue());
            generateNewDataset();
        });
        
        blinkCheckBox = new CheckBox("Abilita lampeggiamento continuo");
        blinkCheckBox.getStyleClass().add("check-box");
        blinkCheckBox.setSelected(false);
        blinkCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> blinkBars = newVal);
        applyCheckBoxAnimations(blinkCheckBox);
        
        Label instructions = new Label("Seleziona e avvia per guardare la magia!");
        instructions.getStyleClass().add("instructions");
        
        panel.getChildren().addAll(startButton, generateButton, sizeLabel, sizeSelector, blinkCheckBox, instructions);
        return panel;
    }
    
    private GridPane createChartsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        
        String[] algorithms = {
            "Bubble Sort", "Selection Sort", "Insertion Sort",
            "Quick Sort", "Merge Sort", "Heap Sort",
            "Shell Sort", "Binary Insertion Sort", "Counting Sort"
        };
        String[] descriptions = {
            "Confronta elementi adiacenti e li scambia se necessario",
            "Trova il minimo e lo posiziona all'inizio",
            "Inserisce ogni elemento nella posizione corretta",
            "Divide l'array e ordina ricorsivamente",
            "Divide e fonde sottovettori ordinati",
            "Costruisce un heap e lo estrae iterativamente",
            "Utilizza gap decrescenti per confronti distanziati",
            "Inserisce con ricerca binaria per trovare la posizione giusta",
            "Conta le occorrenze e ricostruisce l'array"
        };
        
        int row = 0, col = 0;
        for (int i = 0; i < algorithms.length; i++) {
            VBox container = createAlgorithmContainer(algorithms[i], descriptions[i], algorithmColors[i]);
            grid.add(container, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
        return grid;
    }
    
    private VBox createAlgorithmContainer(String name, String desc, Color color) {
        VBox container = new VBox(6);
        container.getStyleClass().add("algorithm-container");
        container.setAlignment(Pos.CENTER);
        container.setCache(true);
        container.setCacheHint(CacheHint.SPEED);
        
        Label title = new Label(name);
        title.getStyleClass().add("algorithm-title");
        title.setTextFill(color);
        
        Label description = new Label(desc);
        description.getStyleClass().add("algorithm-description");
        
        Label status = new Label("⏸️ Pronto");
        status.getStyleClass().add("status-label");
        statusLabels.put(name, status);
        
        Label time = new Label("⏱️ Tempo: 0ms");
        time.getStyleClass().add("time-label");
        timeLabels.put(name, time);
        
        Pane visualPane = createVisualizationPane();
        visualPanes.put(name, visualPane);
        
        // Hover sul container
        ScaleTransition hoverEnlarge = new ScaleTransition(Duration.millis(200), container);
        hoverEnlarge.setToX(1.03);
        hoverEnlarge.setToY(1.03);
        ScaleTransition hoverShrink = new ScaleTransition(Duration.millis(200), container);
        hoverShrink.setToX(1.0);
        hoverShrink.setToY(1.0);
        
        container.setOnMouseEntered(e -> hoverEnlarge.playFromStart());
        container.setOnMouseExited(e -> hoverShrink.playFromStart());
        
        container.getChildren().addAll(title, description, status, time, visualPane);
        return container;
    }
    
    private Pane createVisualizationPane() {
        Pane pane = new Pane();
        pane.setPrefSize(350, 250);
        pane.getStyleClass().add("visualization-pane");
        return pane;
    }
    
    private void startSorting() {
        if (isSortedState) {
            generateNewDataset();
        }
        sizeSelector.setDisable(true);
        startButton.setDisable(true);
        generateButton.setDisable(true);
        
        Platform.runLater(() -> {
            for (String alg : statusLabels.keySet()) {
                statusLabels.get(alg).setText("⏳ In coda");
                statusLabels.get(alg).getStyleClass().remove("completed");
                timeLabels.get(alg).setText("⏱️ Tempo: 0ms");
                finalTimes.remove(alg);
            }
        });
        
        businessLogic.startSorting(new SortingCallback() {
            @Override
            public void onProgress(String algorithm, List<Integer> currentState, List<Integer> activeIndices, long elapsedTime, boolean isCompleted) {
                Platform.runLater(() -> {
                    if ("⏳ In coda".equals(statusLabels.get(algorithm).getText())) {
                        statusLabels.get(algorithm).setText("🔄 In esecuzione...");
                    }
                    updateVisualization(algorithm, currentState, activeIndices, isCompleted);
                    timeLabels.get(algorithm).setText("⏱️ Tempo: " + elapsedTime + "ms");
                    if (isCompleted) {
                        statusLabels.get(algorithm).setText("✅ Completato!");
                        statusLabels.get(algorithm).getStyleClass().add("completed");
                        finalTimes.put(algorithm, elapsedTime);
                    }
                });
            }
            
            @Override
            public void onAllCompleted() {
                Platform.runLater(() -> {
                    sizeSelector.setDisable(false);
                    startButton.setDisable(false);
                    generateButton.setDisable(false);
                    isSortedState = true;
                    animateAndReorder();
                });
            }
        });
    }
    
    private void generateNewDataset() {
        businessLogic.generateNewDataset();
        isSortedState = false;
        finalTimes.clear();
        List<Integer> data = businessLogic.getOriginalData();
        for (String alg : visualPanes.keySet()) {
            updateVisualization(alg, data, Collections.emptyList(), false);
            statusLabels.get(alg).setText("⏸️ Pronto");
            statusLabels.get(alg).getStyleClass().remove("completed");
            timeLabels.get(alg).setText("⏱️ Tempo: 0ms");
        }
    }
    
    /**
     * Aggiorna la visualizzazione per un algoritmo.
     * Se isCompleted==true, non si applica nessun effetto di lampeggio,
     * indipendentemente dallo stato di blinkBars.
     */
    private void updateVisualization(String algorithm, List<Integer> data, List<Integer> activeIndices, boolean isCompleted) {
        Pane pane = visualPanes.get(algorithm);
        pane.getChildren().clear();
        if (data.isEmpty()) return;
        
        double paneWidth = pane.getPrefWidth() - 20;
        double paneHeight = pane.getPrefHeight() - 40;
        double barWidth = paneWidth / data.size();
        int maxValue = data.stream().max(Integer::compareTo).orElse(1);
        Color algColor = getAlgorithmColor(algorithm);
        
        for (int i = 0; i < data.size(); i++) {
            int value = data.get(i);
            double barHeight = ((double) value / maxValue) * paneHeight;
            Rectangle bar = new Rectangle();
            bar.setWidth(barWidth - 2);
            bar.setHeight(barHeight);
            bar.setX(i * barWidth + 10);
            bar.setY(paneHeight - barHeight + 10);
            // Impostazione di default dell'opacità
            bar.setOpacity(1.0);
            
            // Esegui l'animazione del colore solo se il lampeggiamento è abilitato
            if (blinkBars) {
                FillTransition ft = new FillTransition(Duration.millis(300), bar,
                    algColor.deriveColor(0, 1, 1, 0.6),
                    algColor.deriveColor(0, 1, 1, 1.0)
                );
                ft.setCycleCount(1);
                ft.play();
            } else {
                bar.setFill(algColor.deriveColor(0, 1, 1, 1.0));
            }
            
            double intensity = 0.6 + (0.4 * value / maxValue);
            Color barColor = Color.color(
                algColor.getRed() * intensity,
                algColor.getGreen() * intensity,
                algColor.getBlue() * intensity
            );
            // Se il lampeggiamento è disattivato, imposta immediatamente il colore finale
            if (!blinkBars) {
                bar.setFill(barColor);
            }
            
            if (activeIndices.contains(i)) {
                bar.getStyleClass().add("active-bar");
            } else {
                bar.setStroke(Color.DARKGRAY);
                bar.setStrokeWidth(0.5);
                
                // Applica l'effetto di lampeggiamento solo se abilitato e se non è in stato Completato
                if (blinkBars && !isCompleted) {
                    FadeTransition blink = new FadeTransition(Duration.millis(500), bar);
                    blink.setFromValue(1.0);
                    blink.setToValue(0.4);
                    blink.setAutoReverse(true);
                    blink.setCycleCount(Animation.INDEFINITE);
                    blink.play();
                } else {
                    bar.setOpacity(1.0);
                }
            }
            
            Text valueText = new Text(String.valueOf(value));
            valueText.setX(i * barWidth + barWidth / 2 - 5 + 10);
            valueText.setY(paneHeight - barHeight + 5);
            valueText.setFont(Font.font("Consolas", 10));
            valueText.setFill(Color.web("#FFFFFF", 0.9));
            
            Text indexText = new Text(String.valueOf(i));
            indexText.setX(i * barWidth + barWidth / 2 - 3 + 10);
            indexText.setY(paneHeight + 25);
            indexText.setFont(Font.font("Consolas", 9));
            indexText.setFill(Color.web("#CCCCCC", 0.7));
            
            pane.getChildren().addAll(bar, valueText, indexText);
        }
    }
    
    private Color getAlgorithmColor(String algorithm) {
        switch (algorithm) {
            case "Bubble Sort": return algorithmColors[0];
            case "Selection Sort": return algorithmColors[1];
            case "Insertion Sort": return algorithmColors[2];
            case "Quick Sort": return algorithmColors[3];
            case "Merge Sort": return algorithmColors[4];
            case "Heap Sort": return algorithmColors[5];
            case "Shell Sort": return algorithmColors[6];
            case "Binary Insertion Sort": return algorithmColors[7];
            case "Counting Sort": return algorithmColors[8];
            default: return Color.GRAY;
        }
    }
    
    private void animateAndReorder() {
        List<String> sortedAlgs = finalTimes.entrySet().stream()
            .sorted(Comparator.comparingLong(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        ParallelTransition fadeOutAll = new ParallelTransition();
        for (String alg : sortedAlgs) {
            Pane pane = visualPanes.get(alg);
            VBox container = (VBox) pane.getParent();
            FadeTransition ft = new FadeTransition(Duration.millis(300), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            fadeOutAll.getChildren().add(ft);
        }
        
        fadeOutAll.setOnFinished(evt -> {
            chartsGrid.getChildren().clear();
            int row = 0, col = 0;
            for (String alg : sortedAlgs) {
                VBox container = (VBox) visualPanes.get(alg).getParent();
                chartsGrid.add(container, col, row);
                col++;
                if (col > 2) {
                    col = 0;
                    row++;
                }
            }
            ParallelTransition fadeInAll = new ParallelTransition();
            for (String alg : sortedAlgs) {
                Pane pane = visualPanes.get(alg);
                VBox container = (VBox) pane.getParent();
                FadeTransition ftIn = new FadeTransition(Duration.millis(300), container);
                ftIn.setFromValue(0.0);
                ftIn.setToValue(1.0);
                fadeInAll.getChildren().add(ftIn);
            }
            fadeInAll.play();
        });
        
        fadeOutAll.play();
    }
    
    private void playExitAnimation(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(800), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        
        RotateTransition rotate = new RotateTransition(Duration.millis(800), node);
        rotate.setByAngle(360);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(800), node);
        scale.setToX(0.0);
        scale.setToY(0.0);
        
        ParallelTransition exitTransition = new ParallelTransition(fade, rotate, scale);
        exitTransition.setOnFinished(e -> Platform.exit());
        exitTransition.play();
    }
    
    private void applyButtonAnimations(Button btn, boolean isStart) {
        ScaleTransition hoverOn = new ScaleTransition(Duration.millis(200), btn);
        hoverOn.setToX(1.1);
        hoverOn.setToY(1.1);
        ScaleTransition hoverOff = new ScaleTransition(Duration.millis(200), btn);
        hoverOff.setToX(1.0);
        hoverOff.setToY(1.0);
        
        btn.setOnMouseEntered(e -> hoverOn.playFromStart());
        btn.setOnMouseExited(e -> hoverOff.playFromStart());
        
        btn.setOnMousePressed(e -> {
            Circle ripple = new Circle(0, Color.web("#FFFFFF", 0.3));
            ripple.setMouseTransparent(true);
            ripple.setCenterX(e.getX());
            ripple.setCenterY(e.getY());
            ((Pane) btn.getParent()).getChildren().add(ripple);
            
            ScaleTransition rippleScale = new ScaleTransition(Duration.millis(400), ripple);
            rippleScale.setToX(8);
            rippleScale.setToY(8);
            FadeTransition rippleFade = new FadeTransition(Duration.millis(400), ripple);
            rippleFade.setFromValue(0.6);
            rippleFade.setToValue(0.0);
            
            ParallelTransition pt = new ParallelTransition(rippleScale, rippleFade);
            pt.setOnFinished(ev -> ((Pane) btn.getParent()).getChildren().remove(ripple));
            pt.play();
            
            ScaleTransition press = new ScaleTransition(Duration.millis(100), btn);
            press.setToX(0.95);
            press.setToY(0.95);
            press.playFromStart();
        });
        
        btn.setOnMouseReleased(e -> {
            ScaleTransition release = new ScaleTransition(Duration.millis(100), btn);
            release.setToX(1.0);
            release.setToY(1.0);
            release.playFromStart();
        });
        
        if (isStart) {
            ScaleTransition pulseUp = new ScaleTransition(Duration.millis(800), btn);
            pulseUp.setToX(1.05);
            pulseUp.setToY(1.05);
            pulseUp.setAutoReverse(true);
            pulseUp.setCycleCount(Animation.INDEFINITE);
            pulseUp.play();
            
            DropShadow glow = new DropShadow(BlurType.GAUSSIAN, Color.web("#FFD700"), 20, 0.6, 0, 0);
            glow.setSpread(0.7);
            btn.setEffect(glow);
            
            FadeTransition fadeGlow = new FadeTransition(Duration.millis(800), btn);
            fadeGlow.setFromValue(1.0);
            fadeGlow.setToValue(0.7);
            fadeGlow.setAutoReverse(true);
            fadeGlow.setCycleCount(Animation.INDEFINITE);
            fadeGlow.play();
        }
    }
    
    private void applyComboBoxAnimations(ComboBox<?> combo) {
        ScaleTransition hoverOn = new ScaleTransition(Duration.millis(200), combo);
        hoverOn.setToX(1.05);
        hoverOn.setToY(1.05);
        ScaleTransition hoverOff = new ScaleTransition(Duration.millis(200), combo);
        hoverOff.setToX(1.0);
        hoverOff.setToY(1.0);
        
        combo.setOnMouseEntered(e -> hoverOn.playFromStart());
        combo.setOnMouseExited(e -> hoverOff.playFromStart());
    }
    
    private void applyCheckBoxAnimations(CheckBox cb) {
        ScaleTransition hoverOn = new ScaleTransition(Duration.millis(200), cb);
        hoverOn.setToX(1.05);
        hoverOn.setToY(1.05);
        ScaleTransition hoverOff = new ScaleTransition(Duration.millis(200), cb);
        hoverOff.setToX(1.0);
        hoverOff.setToY(1.0);
        
        cb.setOnMouseEntered(e -> hoverOn.playFromStart());
        cb.setOnMouseExited(e -> hoverOff.playFromStart());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public interface SortingCallback {
        void onProgress(String algorithm, List<Integer> currentState, List<Integer> activeIndices,
                        long elapsedTime, boolean isCompleted);
        void onAllCompleted();
    }
}