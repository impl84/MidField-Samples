
package performer;

import java.io.IOException;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;

import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: SimpleVideoChat
 *
 * Date Modified: 2021.11.05
 *
 */
public class SimpleVideoChat
{
// =============================================================================
// CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - MAIN METHOD -----------------------------------------------------------
    //
    public static void main(String[] args)
    {
        MfsNode            mfs      = null;
        AbstractSampleCode sender   = null;
        AbstractSampleCode receiver = null;
        ConsoleReader      reader   = ConsoleReader.getInstance();
        ConsolePrinter     printer  = ConsolePrinter.getInstance();
        
        try {
            // MidField System ������������D
            mfs = MfsNode.initialize();		// SystemException
            
            // MidField System ���N������D
            mfs.activate();					// SystemException
            
            // ���o�͍\���c�[���𐶐�����D
            ConfigTool cfgTool = new ConfigTool(reader, printer);
            
            // �r�f�I���M�p�̃T���v���R�[�h�����s����D
            sender = new DeviceToNetworkEx();
            sender.open(cfgTool);		// SystemException, StreamException
            
            // �r�f�I��M�p�̃T���v���R�[�h�����s����D
            receiver = new NetworkToRendererEx();
            receiver.open(cfgTool);
            
            // Enter�L�[�̓��͂�҂D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            reader.readLine();	// IOException
        }
        catch (SystemException ex) {
            // MidField System �̓����������ɗ�O�����������D
            // ��O�������̃X�^�b�N�g���[�X��\������D
            ex.printStackTrace();
        }
        catch (IOException ex) {
            // Enter�L�[���͎��ɗ�O�����������D�܂��͏�L�ȊO�̗�O�����������D
            // ��O�������̃X�^�b�N�g���[�X��\������D
            ex.printStackTrace();
        }
        finally {
            // �T���v���R�[�h�̏������I������D
            if (receiver != null) { receiver.close(); }
            if (sender != null) { sender.close(); }
            
            // MidField System ���I������D
            if (mfs != null) { mfs.shutdown(); }
            
            // �W�����͂����1�s�ǂݍ��݂ɗ��p���� ConsoleReader ���������D
            reader.release();
        }
    }
}