package App.controller.strategies;

import App.view.ViewController;

public class PrintPrevPage implements AppStrategy {
    private final ViewController viewController;

    public PrintPrevPage(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public void execute() {
        viewController.printPrevious();
    }
}
