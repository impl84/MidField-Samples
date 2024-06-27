
package util;

import com.midfield_system.api.log.LogPrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.08.24
 *
 */
public class ConsolePrinter
    implements
        LogPrinter
{
// =============================================================================
// CLASS VARIABLE:
// =============================================================================
    
    // - PRIVATE STATIC VARIABLE -----------------------------------------------
    private static ConsolePrinter theInstance = null;
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static ConsolePrinter getInstance()
    {
        // ConsolePrinter のインスタンスの有無を確認する．
        if (ConsolePrinter.theInstance == null) {
            // インスタンスが存在しない場合は，
            // ConsolePrinter のインスタンスを生成する．
            ConsolePrinter.theInstance = new ConsolePrinter();
        }
        // ConsoleReader のインスタンスを返す．
        return ConsolePrinter.theInstance;
        
    }
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: LineReader
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: LogPrinter
    //
    @Override
    public void printf(String format, Object... args)
    {
        System.out.printf(format, args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: LogPrinter
    //
    @Override
    public void println(Object obj)
    {
        System.out.println(obj);
    }
}
