
package trial.console;

import java.util.List;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.protocol.StreamInfo;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.08.19
 *
 */
public class InputSettingsMenu
    extends
        ConsoleMenu
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MENU_TITLE  = "���V�X�e�� �����͐ݒ� ���o�͐ݒ� ���X�g���[������";
    private static final String MENU_PROMPT = " in";
    
    private static final String ADD_CAPTURE_DEVICE         = "���̓f�o�C�X�ǉ�";
    private static final String REFRESH_SOURCE_STREAM_INFO = "��M�X�g���[�������W";
    private static final String CONFIGURE_INCOMING_STREAM  = "��M�X�g���[���ǉ�";
    private static final String TRANSITION_TO_OUTPUT       = "�o�͐ݒ�       �� ���o�͐ݒ�";
    private static final String SHOW_SEGMENT_IO            = "���o�͐ݒ�\��";
    private static final String QUIT                       = "�ݒ�L�����Z�� �� ���V�X�e��";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private SystemMenu         systemMenu         = null;
    private OutputSettingsMenu outputSettingsMenu = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public InputSettingsMenu()
    {
        //
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void setStateInstance(SystemMenu sysMenu, OutputSettingsMenu outMenu)
    {
        this.systemMenu = sysMenu;
        this.outputSettingsMenu = outMenu;
    }
    
// -----------------------------------------------------------------------------
// PROTECTED METHOD:
// -----------------------------------------------------------------------------
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected MenuItem[] initMenuItems()
    {
        MenuItem[] menuItems = new MenuItem[] {
            new MenuItem(0, ADD_CAPTURE_DEVICE, () -> addCaptureDevice()),
            new MenuItem(),
            new MenuItem(1, REFRESH_SOURCE_STREAM_INFO, () -> refreshSourceStreamInfo()),
            new MenuItem(2, CONFIGURE_INCOMING_STREAM, () -> configureIncomingStream()),
            new MenuItem(),
            new MenuItem(3, TRANSITION_TO_OUTPUT, () -> transitionToOutput()),
            new MenuItem(),
            new MenuItem(8, SHOW_SEGMENT_IO, () -> showSegmentIo()),
            new MenuItem(9, QUIT, () -> quit())
        };
        return menuItems;
    }
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected String getMenuTitle()
    {
        return MENU_TITLE;
    }
    
    // - PROTECTED METHOD ------------------------------------------------------
    //
    @Override
    protected String getPrompt()
    {
        return MENU_PROMPT;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu addCaptureDevice()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // ���̓f�o�C�X��񃊃X�g���擾����D
        DeviceInfoManager devMgr   = DeviceInfoManager.getInstance();
        List<DeviceInfo>  lsDevInf = devMgr.getDeviceInfoList();
        int               size     = lsDevInf.size();
        if (size <= 0) {
            warningPause("���p�\�ȓ��̓f�o�C�X������܂���D\n");
            return this;
        }
        // ���̓f�o�C�X��񃊃X�g�̊e�v�f��\������D
        printDeviceInfoList(lsDevInf);
        
        // ���̓f�o�C�X��񃊃X�g�̗v�f�ԍ���I������D
        int num = selectNumber("���̓f�o�C�X�ԍ�");
        if (num >= size) {
            warningPause("�L�����Z�����܂����D\n");
            return this;
        }
        // �I�����ꂽ���̓f�o�C�X�����擾����D
        DeviceInfo devInf = lsDevInf.get(num);
        
        // �I�����ꂽ���̓f�o�C�X��񂩂�o�̓t�H�[�}�b�g��񃊃X�g���擾����D
        List<StreamFormat> lsStmFmt = devInf.getOutputFormatList();
        size = lsStmFmt.size();
        if (size <= 0) {
            warningPause("���p�\�ȏo�̓t�H�[�}�b�g������܂���D\n");
            return this;
        }
        // �o�̓t�H�[�}�b�g��񃊃X�g�̊e�v�f��\������D
        printStreamFormatList(lsStmFmt);
        
        // �o�̓t�H�[�}�b�g��񃊃X�g�̗v�f�ԍ���I������D
        num = selectNumber("�t�H�[�}�b�g�ԍ�");
        if (num >= size) {
            warningPause("�L�����Z�����܂����D\n");
            return this;
        }
        // �I�������o�̓t�H�[�}�b�g�����擾����D
        StreamFormat stmFmt = lsStmFmt.get(num);
        
        // ���̓f�o�C�X�E�o�̓t�H�[�}�b�g���� SegmentIo �֒ǉ����C
        // ���݂� SegmentIo �̓��o�͏���\������D
        segIo.addInputDevice(devInf, stmFmt);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu refreshSourceStreamInfo()
    {
        // �\�[�X�z�X�g����ݒ肷��D
        // ���X�y�[�X��؂�ŕ����w���
        print("\n");
        message("���M���z�X�g��/IP�A�h���X����͂��ĉ������D\n");
        print("  �����w�肷��ꍇ�̓X�y�[�X�ŋ�؂��ē��͂��ĉ������D\n");
        String   buf      = getLine("���M���z�X�g��/IP�A�h���X");
        String[] srcAddrs = buf.split("\\s+");
        
        // ���M���X�g���[�����v���p�P�b�g�𑗐M����D
        messagePause("���M���X�g���[�����v���p�P�b�g�𑗐M���܂��D\n");
        StreamInfoManager manager = StreamInfoManager.getInstance();
        manager.refreshSourceStreamInfoList(srcAddrs);
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu configureIncomingStream()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // ���M���X�g���[�����̃��X�g���擾����D
        StreamInfoManager manager  = StreamInfoManager.getInstance();
        List<StreamInfo>  lsStmInf = manager.getSourceStreamInfoList();
        int               size     = lsStmInf.size();
        if (size <= 0) {
            warningPause("��M�\�ȃX�g���[��������܂���D\n");
            return this;
        }
        // ���M���X�g���[�����̔z��̊e�v�f��\������D
        printStreamInfoList("��M�ł���X�g���[���ꗗ", lsStmInf);
        
        // ���M���X�g���[�����̔z��v�f�ԍ���I������D
        int num = selectNumber("�X�g���[���ԍ�");
        if (num >= size) {
            warningPause("�L�����Z�����܂����D\n");
            return this;
        }
        // �I�����ꂽ���M���X�g���[������ SegmentIo �̓��͂Ƃ��Đݒ肵�C
        // ���݂� SegmentIo �̓��o�͏���\������D
        StreamInfo stmInf = lsStmInf.get(num);
        segIo.configureIncomingStream(stmInf);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu transitionToOutput()
    {
        ConsoleMenu nextMenu = null;
        SegmentIo   segIo    = ConsoleMenu.getSegmentIo();
        
        List<IoParam> list = segIo.getInputParamList();
        if (list.isEmpty()) {
            // ���͂��ݒ肳��Ă��Ȃ��ꍇ�C���̃��j���[�� InputSettingsMenu �̂܂܁D
            warningPause("���͂��ݒ肳��Ă��܂���D\n");
            nextMenu = this;
        }
        else {
            // ���͂��ݒ肳��Ă���ꍇ�C���̃��j���[�� OutputSettingsMenu �ɂȂ�D
            nextMenu = this.outputSettingsMenu;
        }
        return nextMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // ���̃��j���[�ƂȂ� SystemMenu ��Ԃ��D
        return this.systemMenu;
    }
}
