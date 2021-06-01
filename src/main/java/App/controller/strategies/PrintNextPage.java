package App.controller.strategies;


import App.view.ViewController;

public class PrintNextPage implements AppStrategy {
    private final ViewController viewController;

    public PrintNextPage(ViewController viewController) {
        this.viewController = viewController;
    }

    @Override
    public void execute() {
        viewController.printNext();
    }
}
