
package console;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.AudioFormat;
import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.IoFormat;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.stream.VideoFormat;
import com.midfield_system.api.system.SystemException;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: OutputSettingsMenu
 *
 * Date Modified: 2021.08.23
 *
 */
public class OutputSettingsMenu
    extends
        ConsoleMenu
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String MENU_TITLE  = "���V�X�e�� �����͐ݒ� ���o�͐ݒ� ���X�g���[������";
    private static final String MENU_PROMPT = "out";
    
    private static final String SET_DEFAULT_RENDERER      = "�Đ�/�\���ݒ�";
    private static final String CONFIGURE_OUTGOING_STREAM = "���M�X�g���[���ݒ�";
    private static final String SETUP_STREAM              = "�X�g���[������ �� ���X�g���[������";
    private static final String SHOW_SEGMENT_IO           = "���o�͐ݒ�\��";
    private static final String QUIT                      = "�ݒ�L�����Z�� �� ���V�X�e��";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private SystemMenu        systemMenu        = null;
    private StreamControlMenu streamControlMenu = null;
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public OutputSettingsMenu()
    {
        //
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void setStateInstanue(SystemMenu sysMenu, StreamControlMenu stmMenu)
    {
        this.systemMenu = sysMenu;
        this.streamControlMenu = stmMenu;
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
            new MenuItem(0, SET_DEFAULT_RENDERER, () -> setDefaultRenderer()),
            new MenuItem(1, CONFIGURE_OUTGOING_STREAM, () -> configureOutgoingStream()),
            new MenuItem(),
            new MenuItem(2, SETUP_STREAM, () -> setupStream()),
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
    private ConsoleMenu setDefaultRenderer()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // SegmentIo �̏o�͂Ƃ��ăf�t�H���g�����_����ݒ肷��D
        segIo.configureDefaultRenderer();
        
        // ���C�u�\�[�X�I�v�V������L���ɂ���D
        segIo.setLiveSource(true);
        
        // ���݂� SegmentIo �̓��o�͏���\������D
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu configureOutgoingStream()
    {
        SegmentIo segIo = ConsoleMenu.getSegmentIo();
        
        // SegmentIo �ɐݒ肳��Ă�����͏������ƂɁC
        // ���p�\�ȏo�̓t�H�[�}�b�g���̃��X�g���擾����D
        List<StreamFormat> lsOutFmt = segIo.getOutputVideoFormatList();
        lsOutFmt.addAll(segIo.getOutputAudioFormatList());
        int size = lsOutFmt.size();
        if (size <= 0) {
            warningPause("���p�\�ȏo�̓t�H�[�}�b�g������܂���D\n");
            return this;
        }
        // �o�̓t�H�[�}�b�g��񃊃X�g�̊e�v�f��\������D
        printStreamFormatList(lsOutFmt);
        
        // �o�̓t�H�[�}�b�g��񃊃X�g�Ƀr�f�I�t�H�[�}�b�g���܂܂�Ă���ꍇ
        // �r�f�I�t�H�[�}�b�g��I������D
        VideoFormat vidFmt = selectVideoFormat(lsOutFmt);
        
        // �o�̓t�H�[�}�b�g��񃊃X�g�ɃI�[�f�B�I�t�H�[�}�b�g���܂܂�Ă���ꍇ
        // �I�[�f�B�I�t�H�[�}�b�g��I������D
        // ���I���ς݂̃r�f�I�t�H�[�}�b�g�� DV/M2TS �̏ꍇ�́C
        // �I�[�f�B�I�t�H�[�}�b�g��I�����Ȃ��D
        AudioFormat audFmt = selectAudioFormat(vidFmt, lsOutFmt);
        if ((vidFmt == null) && (audFmt == null)) {
            return this;
        }
        // SegmentIo �̏o�͂��C���M�X�g���[���Ƃ��č\������D
        segIo.configureOutgoingStream(vidFmt, audFmt);
        
        // �g�����X�|�[�g�v���g�R����I������D
        ProtocolType type          = ProtocolType.UDP;
        boolean      isTcpSelected = selectTransportProtocol();
        if (isTcpSelected) {
            type = ProtocolType.TCP;
        }
        // �ڑ��̕�����I������(TCP���p���̂�)�D
        boolean isRcvCon = false;
        if (isTcpSelected) {
            isRcvCon = selectConnectionDirection();
        }
        ConnectionMode mode = ConnectionMode.ACTIVE;
        if (isRcvCon) {
            mode = ConnectionMode.PASSIVE;
        }
        // �g�����X�|�[�g�v���g�R���Ɛڑ��̕�����ݒ肵�C
        // ���݂� SegmentIo �̓��o�͏���\������D
        segIo.setTransportProtocol(type, mode);
        showSegmentIo();
        
        return this;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private VideoFormat selectVideoFormat(List<StreamFormat> lsOutFmt)
    {
        VideoFormat vidFmt = null;
        
        // �^����ꂽ StreamFormat �̃��X�g��
        // �r�f�I�t�H�[�}�b�g���܂܂�Ă��邩���m�F����D
        boolean isVid = false;
        for (StreamFormat stmFmt : lsOutFmt) {
            if (stmFmt instanceof VideoFormat) {
                isVid = true;
                break;
            }
        }
        if (isVid == false) {
            // ���p�\�ȃr�f�I�t�H�[�}�b�g�������D
            return vidFmt;
        }
        // �r�f�I�t�H�[�}�b�g�ԍ���I������D
        int size = lsOutFmt.size();
        int num  = selectNumber("�r�f�I�t�H�[�}�b�g�ԍ�");
        if (num >= size) {
            warningPause("�R�}���h���L�����Z�����܂����D\n");
            return vidFmt;
        }
        // �I�����ꂽ�r�f�I�t�H�[�}�b�g�����擾����D
        StreamFormat stmFtm = lsOutFmt.get(num);
        if (stmFtm instanceof VideoFormat) {
            vidFmt = (VideoFormat)stmFtm;
        }
        else {
            warningPause("�r�f�I�t�H�[�}�b�g�ł͂���܂���D\n");
        }
        return vidFmt;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private AudioFormat selectAudioFormat(
        VideoFormat vidFmt, List<StreamFormat> lsOutFmt
    )
    {
        AudioFormat audFmt = null;
        
        // �I���ς݂̃r�f�I�t�H�[�}�b�g�� DV/M2TS �̏ꍇ�́C
        // �I�[�f�B�I�t�H�[�}�b�g��I�����Ȃ��D
        if (vidFmt != null) {
            IoFormat ioFmt = vidFmt.getIoFormat();
            if ((ioFmt == IoFormat.DVSD) || (ioFmt == IoFormat.M2TS)) {
                return audFmt;
            }
        }
        // �^����ꂽ StreamFormat �̃��X�g��
        // �I�[�f�B�I�t�H�[�}�b�g���܂܂�Ă��邩���m�F����D
        boolean isAud = false;
        for (StreamFormat stmFmt : lsOutFmt) {
            if (stmFmt instanceof AudioFormat) {
                isAud = true;
                break;
            }
        }
        if (isAud == false) {
            // ���p�\�ȃI�[�f�B�I�t�H�[�}�b�g�������D
            return audFmt;
        }
        // �I�[�f�B�I�t�H�[�}�b�g�ԍ���I������D
        int size = lsOutFmt.size();
        int num  = selectNumber("�I�[�f�B�I�t�H�[�}�b�g�ԍ�");
        if (num >= size) {
            warningPause("�R�}���h���L�����Z�����܂����D\n");
            return audFmt;
        }
        // �I�����ꂽ�I�[�f�B�I�t�H�[�}�b�g�����擾����D
        StreamFormat stmFtm = lsOutFmt.get(num);
        if (stmFtm instanceof AudioFormat) {
            audFmt = (AudioFormat)stmFtm;
        }
        else {
            warningPause("�I�[�f�B�I�t�H�[�}�b�g�ł͂���܂���D\n");
        }
        return audFmt;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private boolean selectTransportProtocol()
    {
        boolean useTCP = false;
        
        printListTitle("�X�g���[���]���v���g�R��");
        print("  [0] TCP\n");
        print("  [1] UDP\n");
        
        int num = selectNumber("�v���g�R���ԍ�");
        switch (num) {
        case 0:
            useTCP = true;
            break;
        case 1:
            useTCP = false;
            break;
        default:
            warningPause("�R�}���h���L�����Z�����܂����DUDP�𗘗p���܂��D\n");
            useTCP = false;
            break;
        }
        return useTCP;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private boolean selectConnectionDirection()
    {
        boolean isRcvCon = false;
        
        printListTitle("�R�l�N�V�����ڑ�����");
        print("  [0] ��M������ڑ���������\n");
        print("  [1] ���M������ڑ���������\n");
        
        int num = selectNumber("�ԍ�");
        switch (num) {
        case 0:
            isRcvCon = true;
            break;
        case 1:
            isRcvCon = false;
            break;
        default:
            warningPause("�R�}���h���L�����Z�����܂����D��M������ڑ��������܂��D\n");
            isRcvCon = true;
            break;
        }
        return isRcvCon;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu setupStream()
    {
        // SegmentIo �̏o�͂��ݒ肳��Ă��邩���m�F����D
        SegmentIo     segIo = ConsoleMenu.getSegmentIo();
        List<IoParam> list  = segIo.getOutputParamList();
        if (list.isEmpty()) {
            // �o�͂��ݒ肳��Ă��Ȃ��ꍇ�� OutputSettingsMenu �̂܂�
            warningPause("�o�͂��ݒ肳��Ă��܂���D\n");
            return this;
        }
        // �v���r���[��L���ɂ���D
        segIo.setPreviewer();
        
        // StreamPerformer �𐶐�����D
        StreamPerformer pfmr;
        try {
            pfmr = StreamPerformer.newInstance(segIo);
            // SystemException, StreamException
            
            // ExPerformerFrame �� EDT ��Ő�������D
            SwingUtilities.invokeLater(() -> { new ExPerformerFrame(pfmr); });
        }
        catch (SystemException | StreamException ex) {
            // �X�g���[���������ɗ�O�����������D
            warning("�X�g���[���𐶐��ł��܂���(%s)�D\n", ex.getMessage());
            return this;
        }
        // �X�g���[���̏������J�n����D
        try {
            pfmr.open();	// StreamException
            pfmr.start();	// StreamException
        }
        catch (StreamException ex) {
            // �X�g���[���̏����J�n���ɃG���[�����������D
            warning("���o�͏������J�n�ł��܂���(%s)�D\n", ex.getMessage());
            return this;
        }
        // ���������X�g���[���̏������J�n���ꂽ�D
        // ���������X�g���[���̃C���X�^���X���CConsoleMenu �ɐݒ肵�Ă����D
        ConsoleMenu.setSelectedStream(pfmr);
        
        // �V���� StreamPerformer ���Z�b�g�A�b�v�ł����ꍇ�C
        // ���̃��j���[�ƂȂ� StreamControlMenu ��Ԃ��D
        return this.streamControlMenu;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private ConsoleMenu quit()
    {
        // ���̃��j���[�ƂȂ� SystemMenu ��Ԃ��D
        return this.systemMenu;
    }
}