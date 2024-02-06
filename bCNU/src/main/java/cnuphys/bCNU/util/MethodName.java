package cnuphys.bCNU.util;

import java.io.PrintStream;

import javax.swing.SwingUtilities;

/**
 * A utility class to print the name of the calling method
 * for debugging
 */
public class MethodName {

	/**
	 * Print the name of the calling method to System.err
     */
	public static void printMethodName() {
	       // Get the current stack trace
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        
        // stackTraceElements[2] is the caller for this method
        // stackTraceElements[0] is getStackTrace, and stackTraceElements[1] is printMethodName itself
        String methodName = stackTraceElements[2].getMethodName();
        
        System.err.println("Current method: " + methodName);
	}
	
	/**
	 * Print the name of the calling method
	 * 
	 * @param out the PrintStream to use
	 */
	public static void printMethodName(PrintStream out) {
        // Get the current stack trace
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        
        // stackTraceElements[2] is the caller for this method
        // stackTraceElements[0] is getStackTrace, and stackTraceElements[1] is printMethodName itself
        String methodName = stackTraceElements[2].getMethodName();
        
        out.println("Current method: " + methodName);
	}
	
	/**
	 * Print the name of the calling method if the calling method is
	 * running on the event dispatch thread (EDT)
	 * @param out the PrintStream to use
     */
	public static void printMethodNameIfEDT(PrintStream out) {
		if (SwingUtilities.isEventDispatchThread()) {
            out.print("CALLED FROM EDT: ");
            // Get the current stack trace
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            
            // stackTraceElements[2] is the caller for this method
            // stackTraceElements[0] is getStackTrace, and stackTraceElements[1] is printMethodName itself
            String methodName = stackTraceElements[2].getMethodName();
            
            out.println("Current method: " + methodName);
       }
    }


}
