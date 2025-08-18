
package grpc.v1.client;

import java.util.Scanner;

public class Reporter
{
    // 使い回すScanner（System.inは閉じない）
    private static final Scanner scanner = new Scanner(System.in);
    
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
    
    public static void message()
    {
        System.out.println();
    }
    
    public static void message(String message)
    {
        System.out.println(message);
    }
    
    public static void message(String format, Object... args)
    {
        System.out.printf(format, args);
    }
    
    public static String readLine(String prompt)
    {
        System.out.print(prompt);
        return Reporter.scanner.nextLine();
    }
    
    public static void waitForEnter()
    {
        System.out.println("> Enter キーの入力を待ちます．");
        Reporter.scanner.nextLine();
    }
    
    public static void warning(String warningMessage)
    {
        System.out.println("WARNING> " + warningMessage);
    }
    
    public static void warning(String errorMessage, Throwable th)
    {
        System.out.println("WARNING> " + errorMessage);
        System.out.println("WARNING> " + th.getMessage());
        var cause = th.getCause();
        if (cause != null) {
            System.out.println("WARNING> cause: " + cause.getMessage());
        }
    }
}
