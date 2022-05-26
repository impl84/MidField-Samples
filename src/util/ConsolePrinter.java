
package util;

import com.midfield_system.api.util.LogPrinter;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ConsolePrinter
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
        // ConsolePrinter �̃C���X�^���X�̗L�����m�F����D
        if (ConsolePrinter.theInstance == null) {
            // �C���X�^���X�����݂��Ȃ��ꍇ�́C
            // ConsolePrinter �̃C���X�^���X�𐶐�����D
            ConsolePrinter.theInstance = new ConsolePrinter();
        }
        // ConsoleReader �̃C���X�^���X��Ԃ��D
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
