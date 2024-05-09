
package app.performer;

import com.midfield_system.api.stream.DeviceInfoManager;
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
public class DeviceToRenderer
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
            
            // �����������_���ŏo�͂��\������D
            segIo.configurePreferredRenderer();
            
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