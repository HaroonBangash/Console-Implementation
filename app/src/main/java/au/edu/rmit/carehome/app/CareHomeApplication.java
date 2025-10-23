package au.edu.rmit.carehome.app;

import au.edu.rmit.carehome.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;

/**
 * Design decisions: JavaFX entry point wiring together bootstrap container and main view.
 */
public class CareHomeApplication extends Application {
    private CareHomeBootstrap bootstrap;
    private CareHomeContainer container;

    @Override
    public void init() {
        bootstrap = new CareHomeBootstrap(Path.of("data", "carehome.ser"), Path.of("data", "archive"));
        container = bootstrap.initialise();
    }

    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView(container);
        Scene scene = new Scene(mainView.getRoot(), 1200, 800);
        stage.setTitle("Resident HealthCare System");
        stage.setScene(scene);
        stage.show();
        mainView.promptLogin(stage);
    }

    @Override
    public void stop() {
        if (bootstrap != null && container != null) {
            bootstrap.persist(container);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
