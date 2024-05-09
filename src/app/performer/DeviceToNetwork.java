
package app.performer;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.midfield.MfsApplication;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.13
 *
 */
public class DeviceToNetwork
{
    public static void main(String[] args)
    {
        StreamPerformer pfmr = null;
        
        try {
            // ��MidField System �̃A�v���P�[�V�������N������D
            var mfsApp = MfsApplication.launch();
            
            // ���r�f�I�ƃI�[�f�B�I�f�o�C�X�œ��͂��\������D
            // �E���̓f�o�C�X��I�����Đݒ肷��D
            var devInfMgr = DeviceInfoManager.getInstance();
            
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
            var segIo = new SegmentIo();
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
            
            //��StreamPerformer �𐶐����CUI�֒ǉ�����D
            pfmr = StreamPerformer.newInstance(segIo);
            mfsApp.addStreamPerformer(pfmr);
            
            // ��StreamPerformer �̓��o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // ��StreamPerformer �̓��o�͏������I������D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            pfmr.stop();
            pfmr.close();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ��StreamPerformer �̑S�Ă̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
        }
    }
}