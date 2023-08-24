
package rpc.performer;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.MfsRemote;
import com.midfield_system.rpc.api.client.RemoteControlException;
import com.midfield_system.rpc.api.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class DeviceToNetwork
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt = null;
        StreamPerformer pfmr   = null;
        
        try {
            // �R�}���h���C����������T�[�o��IP�A�h���X�ƃ|�[�g�ԍ����擾����D
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            
            // ���T�[�o�Ɛڑ����C���u������J�n����D
            mfsRmt = new MfsRemote(serverAddr, serverPort, err -> System.err.println(err));
            mfsRmt.initializeRemoteControl();
            
            // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
            // �E���̓f�o�C�X��I�����Đݒ肷��D
            var devInfMgr = mfsRmt.getDeviceInfoManager();
            
            var lsVidDev = devInfMgr.getVideoInputDeviceInfoList();
            if (lsVidDev.size() <= 0) {
                System.out.println("�����p�\�ȃr�f�I���̓f�o�C�X������܂���D");
                return;
            }
            var lsAudDev = devInfMgr.getAudioInputDeviceInfoList();
            if (lsAudDev.size() <= 0) {
                System.out.println("�����p�\�ȃI�[�f�B�I���̓f�o�C�X������܂���D");
                return;
            }
            var segIo = mfsRmt.newSegmentIo();
            segIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            
            // �����M�X�g���[���ŏo�͂��\������D
            // �E���M�t�H�[�}�b�g��I�����Đݒ肷��D
            // �ETCP�𗘗p���C�R�l�N�V�����ڑ��v�����󂯓����D
            // �E�����v���r���[���\�𗘗p����D
            var lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("�����M�\�ȃr�f�I�t�H�[�}�b�g������܂���D");
                return;
            }
            var lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("�����M�\�ȃI�[�f�B�I�t�H�[�}�b�g������܂���D");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreferredPreviewer();
            
            // ��RcStreamPerformer �𐶐�����D
            pfmr = mfsRmt.newStreamPerformer(segIo);
            
            // ��RcStreamPerformer �̓��o�͏������J�n����D
            pfmr.start();
            
            // ��RcStreamPerformer �̓��o�͏������I������D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            pfmr.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ��RcStreamPerformer �̑S�Ă̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // ���T�[�o�Ɛؒf���C���u������I������D
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}