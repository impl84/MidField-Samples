
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2022.06.08
 *
 */
public class ConsoleReader
    implements
        LineReader
{
// =============================================================================
// CLASS VARIABLE:
// =============================================================================
    
    // - PRIVATE STATIC VARIABLE -----------------------------------------------
    private static ConsoleReader theInstance = null;
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static ConsoleReader getInstance()
    {
        // ConsoleReader のインスタンスの有無を確認する．
        if (ConsoleReader.theInstance == null) {
            // インスタンスが存在しない場合は，
            // ConsoleReader のインスタンスを生成する．
            ConsoleReader.theInstance = new ConsoleReader();
        }
        // ConsoleReader のインスタンスを返す．
        return ConsoleReader.theInstance;
        
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // 標準入力からの1行読み込みに利用する BufferedReader
    private final BufferedReader bufferedReader;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void release()
    {
        try {
            // BufferedReader を閉じる．
            // (IOException)
            this.bufferedReader.close();
        }
        catch (IOException ex) {
            // 例外発生時のスタックトレースを出力する．
            ex.printStackTrace();
        }
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: LineReader
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: LineReader
    //
    @Override
    public String readLine()
        throws IOException
    {
        // 標準入力から1行読み込む．
        // (IOException)
        String line = this.bufferedReader.readLine();
        
        // 読み込んだ1行を返す．
        return line;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleReader()
    {
        // 標準入力からの1行読み込みに利用する BufferedReader を生成する．
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }
}
