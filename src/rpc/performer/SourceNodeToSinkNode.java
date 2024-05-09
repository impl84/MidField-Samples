
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
public class SourceNodeToSinkNode
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RemoteOperator srcOp = null;
        RemoteOperator snkOp = null;
        
        try {
            // �R�}���h���C����������K�v��IP�A�h���X���擾����D
            var srcAddr = args[0];
            var snkAddr = args[1];
            
            // ��Source Node �� Sink Node �ւ̉��u������J�n����D
            srcOp = new RemoteOperator(srcAddr, err -> System.err.println(err));
            snkOp = new RemoteOperator(snkAddr, err -> System.err.println(err));
            
            // �����p�@�\���Z�b�g�A�b�v���C���p�������J�n����D
            // (1) Source Node �̃r�f�I�J�����ƃ}�C�N����l�b�g���[�N��
            // (2) Source Node ���ł̒��p�i�v���r���[�L��j
            // (3) Source Node ���� Sink Node �ւ̒��p�i�v���r���[�L��j
            // (4) Sink Node �ōĐ��\��
            PerformerId pfmrId0 = srcOp.setupDeviceToNetwork();
            PerformerId pfmrId1 = srcOp.setupNetworkToNetwork(pfmrId0);
            PerformerId pfmrId2 = snkOp.setupNetworkToNetwork(pfmrId1);
            PerformerId pfmrId3 = snkOp.setupNetworkToRenderer(pfmrId2);
            
            // �����ꂼ��� StreamPerofrmer ����U��~������ɊJ�n����D
            stopAndStart(srcOp, pfmrId0);
            stopAndStart(srcOp, pfmrId1);
            stopAndStart(snkOp, pfmrId2);
            stopAndStart(snkOp, pfmrId3);
            
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
            // ��Source Node �� Sink Node �ւ̉��u������I������D
            if (snkOp != null) {
                snkOp.shutdownAll();
            }
            if (srcOp != null) {
                srcOp.shutdownAll();
            }
        }
    }
    
    private static void stopAndStart(RemoteOperator remOp, PerformerId pfmrId)
        throws InterruptedException,
            RemoteControlException
    {
        // �w�肳�ꂽ StreamPerformer �̏����� 5�b��Ɉ�U��~���C
        // ����� 5�b�o�߂�����J�n����D
        Thread.sleep(5000);
        remOp.stopPerformer(pfmrId);
        
        Thread.sleep(5000);
        remOp.startPerformer(pfmrId);
    }
}
