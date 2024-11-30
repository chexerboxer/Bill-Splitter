package app;

import javax.swing.JFrame;
import java.awt.*;
import java.io.IOException;

/**
 * The Main class of our application.
 */
public class Main {
    /**
     * Builds and runs the CA architecture of the application.
     * @param args unused arguments
     */
    public static void main(String[] args) throws IOException {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                                            .addChangePasswordView()
                                            .addLoginView()
                                            .addSignupView()
                                            .addDashboardView()
                                            .addChangePasswordUseCase()
                                            .addSignupUseCase()
                                            .addLoginUseCase()
                                            .addChangePasswordUseCase()
                                            .addLogoutUseCase()
                .addDashboardUseCase()
                                            .build();

        application.setSize(new Dimension(1000,700));
        application.pack();
        application.setVisible(true);
    }
}
