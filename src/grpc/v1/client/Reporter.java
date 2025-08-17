
package grpc.v1.client;

import java.util.Scanner;

public class Reporter
{
    public static void error(String errorMessage)
    {
        System.out.println("ERROR> " + errorMessage);
    }
    
    public static void error(String errorMessage, Throwable th)
    {
        System.out.println("ERROR> " + errorMessage);
        System.out.println("ERROR> " + th.getMessage());
        var cause = th.getCause();
        if (cause != null) {
            System.out.println("ERROR> cause: " + cause.getMessage());
        }
    }
    
    public static void printf(String format, Object... args)
    {
        System.out.printf(format, args);
    }

    public static void println()
    {
        System.out.println();
    }
    
    public static void println(String message)
    {
        System.out.println(message);
    }
    
    public static void waitForEnter()
    {
        System.out.println("> Enter キーの入力を待ちます．");
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
        }
    }
    
    public static void warning(String warningMessage)
    {
        System.out.println("WARNING> " + warningMessage);
    }    
}
