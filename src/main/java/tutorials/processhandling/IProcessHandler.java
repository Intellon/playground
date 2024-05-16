package tutorials.processhandling;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 * The ProcessHandler interface defines a general method for executing processes.
 * This allows for flexible implementations of different process handling methods
 * for different operating systems. Each specific implementation must implement
 * this interface and define the execute method accordingly.
 */
interface IProcessHandler {
    void execute();
}
