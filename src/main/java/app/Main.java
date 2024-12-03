package app;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * The Main class of our application.
 */
public class Main {

    private static final int PROGRAM_WIDTH = 1200;
    private static final int PROGRAM_HEIGHT = 700;

    /**
     * Builds and runs the CA architecture of the application.
     * @param args unused arguments
     * @throws IOException if app isn't built
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

        application.setSize(new Dimension(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        application.setVisible(true);
    }
}
