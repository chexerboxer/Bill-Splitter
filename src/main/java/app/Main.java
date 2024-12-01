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
                .addBillDisplayView()
                                            .addChangePasswordUseCase()
                                            .addSignupUseCase()
                                            .addLoginUseCase()
                                            .addChangePasswordUseCase()
                                            .addLogoutUseCase()
                .addDashboardUseCase()
                .addBillDisplayUseCase()
                                            .build();

        application.setSize(new Dimension(1200,700));

        application.setVisible(true);
    }
}
