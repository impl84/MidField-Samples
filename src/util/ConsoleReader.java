
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
        // ConsoleReader �̃C���X�^���X�̗L�����m�F����D
        if (ConsoleReader.theInstance == null) {
            // �C���X�^���X�����݂��Ȃ��ꍇ�́C
            // ConsoleReader �̃C���X�^���X�𐶐�����D
            ConsoleReader.theInstance = new ConsoleReader();
        }
        // ConsoleReader �̃C���X�^���X��Ԃ��D
        return ConsoleReader.theInstance;
        
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // �W�����͂����1�s�ǂݍ��݂ɗ��p���� BufferedReader
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
            // BufferedReader �����D
            // (IOException)
            this.bufferedReader.close();
        }
        catch (IOException ex) {
            // ��O�������̃X�^�b�N�g���[�X���o�͂���D
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
        // �W�����͂���1�s�ǂݍ��ށD
        // (IOException)
        String line = this.bufferedReader.readLine();
        
        // �ǂݍ���1�s��Ԃ��D
        return line;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleReader()
    {
        // �W�����͂����1�s�ǂݍ��݂ɗ��p���� BufferedReader �𐶐�����D
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }
}
