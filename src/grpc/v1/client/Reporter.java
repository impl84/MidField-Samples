
package grpc.v1.client;

import java.util.Scanner;

/**
 * Reporterクラスは、標準出力を用いたメッセージ表示・エラー/警告出力・
 * ユーザー入力受付のためのユーティリティです。
 * 全てstaticメソッドで構成され、インスタンス化不要です。
 */
public class Reporter
{
    /** 標準入力(System.in)からの入力を受け付けるScanner（使い回し、System.inは閉じない） */
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * エラーメッセージを標準出力に表示する。
     * 
     * @param errorMessage
     *        表示するエラーメッセージ
     */
    public static void error(String errorMessage)
    {
        System.out.println("ERROR> " + errorMessage);
    }
    
    /**
     * エラーメッセージと例外情報を標準出力に表示する。
     * 
     * @param errorMessage
     *        表示するエラーメッセージ
     * @param th
     *        例外オブジェクト
     */
    public static void error(String errorMessage, Throwable th)
    {
        System.out.println("ERROR> " + errorMessage);
        System.out.println("ERROR> " + th.getMessage());
        var cause = th.getCause();
        if (cause != null) {
            System.out.println("ERROR> cause: " + cause.getMessage());
        }
    }
    
    /**
     * 空行を標準出力に表示する。
     */
    public static void message()
    {
        System.out.println();
    }
    
    /**
     * 通常メッセージを標準出力に表示する。
     * 
     * @param message
     *        表示するメッセージ
     */
    public static void message(String message)
    {
        System.out.println(message);
    }
    
    /**
     * 書式付きメッセージを標準出力に表示する。
     * 
     * @param format
     *        printf形式のフォーマット文字列
     * @param args
     *        フォーマットに埋め込む引数
     */
    public static void message(String format, Object... args)
    {
        System.out.printf(format, args);
    }
    
    /**
     * プロンプトを表示し、ユーザーから1行入力を受け取る。
     * 
     * @param prompt
     *        入力前に表示するプロンプト文字列
     * 
     * @return 入力された1行の文字列
     */
    public static String readLine(String prompt)
    {
        System.out.print(prompt);
        return Reporter.scanner.nextLine();
    }
    
    /**
     * ユーザーがEnterキーを押すまで待機する。
     * 標準出力に案内を表示し、入力を1行受け取る。
     */
    public static void waitForEnter()
    {
        System.out.println("> Enter キーの入力を待ちます．");
        Reporter.scanner.nextLine();
    }
    
    /**
     * 警告メッセージを標準出力に表示する。
     * 
     * @param warningMessage
     *        表示する警告メッセージ
     */
    public static void warning(String warningMessage)
    {
        System.out.println("WARNING> " + warningMessage);
    }
    
    /**
     * 警告メッセージと例外情報を標準出力に表示する。
     * 
     * @param errorMessage
     *        表示する警告メッセージ
     * @param th
     *        例外オブジェクト
     */
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
