
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.PerformerId;
import com.midfield_system.rpc.api.client.RemoteControlException;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class RemoteStreamingMixer
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RemoteOperator srcOp = null;
        RemoteOperator mixOp = null;
        RemoteOperator snkOp = null;
        
        try {
            // �R�}���h���C����������K�v��IP�A�h���X���擾����D
            var srcAddr   = args[0];
            var mixAddr   = args[1];
            var snkAddr   = args[2];
            var mixerName = args[3];
            
            // ��Source Node�CMixer Node�CSink Node �ւ̉��u������J�n����D
            srcOp = new RemoteOperator(srcAddr, err -> System.err.println(err));
            mixOp = new RemoteOperator(mixAddr, err -> System.err.println(err));
            snkOp = new RemoteOperator(snkAddr, err -> System.err.println(err));
            
            // ���~�L�T�[�̏������J�n����D
            PerformerId mixerId = mixOp.setupMixerToNetwork(mixerName);
            
            // ���~�L�T�[�̏o�͂��l�b�g���[�N�o�R�Ŏ�M���čĐ��\�����J�n����D
            snkOp.setupNetworkToRenderer(mixerId);
            
            // ���~�L�T�[�ւ̓��͂ƂȂ�f���̑��M���J�n����D
            PerformerId sourceId = srcOp.setupDeviceToNetwork();
            
            // ���f������M���āC�~�L�T�[�ւ̓��͂Ƃ���D
            mixOp.setupNetworkToMixer(sourceId, mixerId);
            
            // ���I����҂D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ���S�Ẳ��u������I������D
            if (srcOp != null) {
                srcOp.shutdownAll();
            }
            if (mixOp != null) {
                mixOp.shutdownAll();
            }
            if (snkOp != null) {
                snkOp.shutdownAll();
            }
        }
    }
}
