
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.MfsRemote;
import com.midfield_system.rpc.api.client.RemoteControlException;
import com.midfield_system.rpc.api.client.SegmentIo;
import com.midfield_system.rpc.api.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class MixerToRenderer
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt     = null;
        StreamPerformer mixer      = null;
        StreamPerformer mixerInput = null;
        
        try {
            // �R�}���h���C����������T�[�o��IP�A�h���X�ƃ|�[�g�ԍ����擾����D
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            var mixerName  = args[2];
            
            // ���T�[�o�Ɛڑ����C���u������J�n����D
            mfsRmt = new MfsRemote(serverAddr, serverPort, err -> System.err.println(err));
            mfsRmt.initializeRemoteControl();
            
            // ���~�L�T�[���\�����C�������J�n����D
            // �E�~�L�T�[�̃t�H�[�}�b�g�ɂ́C�V�X�e���v���p�e�B�̐ݒ�l��K�p����D
            // �E�o�͂̓����_���D
            var mixerIo = mfsRmt.newSegmentIo();
            mixerIo.configureStreamingMixer(mixerName);
            mixerIo.configureRenderer();
            
            mixer = mfsRmt.newStreamPerformer(mixerIo);
            mixer.start();
            
            // ���~�L�T�[���́i�r�f�I�J�����ƃ}�C�N�j���\�����C�������J�n����D
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
            SegmentIo mixInIo = mfsRmt.newSegmentIo();
            mixInIo.configureInputDevice(lsVidDev.get(0), lsAudDev.get(0));
            mixInIo.configureMixerInput(mixer.getPerformerId());
            mixerInput = mfsRmt.newStreamPerformer(mixInIo);
            mixerInput.start();
            
            // �����o�͏������I������D
            System.out.printf("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            // ���������Ƃ肠�������̂܂܂ɂ��Ă����āC
            // ��O�̓`���������ǂ���D��������
            mixerInput.stop();
            mixer.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ���~�L�T�[���͂ƃ~�L�T�[�̑S�Ă̏������I������D
            if (mixerInput != null) {
                mixerInput.delete();
            }
            if (mixer != null) {
                mixer.delete();
            }
            // ���T�[�o�Ɛؒf���C���u������I������D
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}