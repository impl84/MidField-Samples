
package remocon;

import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_MESSAGE;
import static com.midfield_system.gui.misc.GuiConstants.BDR_EMPTY_8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.midfield_system.api.system.rpc.ErrorResponseHandler;
import com.midfield_system.api.system.rpc.RpcClient;
import com.midfield_system.api.system.rpc.RpcRequest;
import com.midfield_system.api.system.rpc.RpcResponse;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.util.TaskExecutor;
import com.midfield_system.gui.misc.MessagePanel;
import com.midfield_system.gui.misc.PopupMessage;

import remocon.ServerAddressPanel.ConnectionState;
import util.AppUtilities;

/*----------------------------------------------------------------------------*/
/**
 * RemoteController
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.12 Koji Hashimoto
 *
 */
@SuppressWarnings("serial")
public class RemoteController
    extends
        JFrame
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String STR_PROGRAM_NAME = Messages.getString("RemoteController.0");
    private static final String STR_TITLE_FORMAT = STR_PROGRAM_NAME + " (%s)";
    private static final String STR_CANT_CONNECT = Messages.getString("RemoteController.1");
    public static final String  LINE_SEPARATOR   = Messages.getString("RemoteController.3");
    private static final String LINE_HEADER      = Messages.getString("RemoteController.4");
    
    private static final int RPC_PORT_NUMBER = 60202;
    
    private static final int PRE_FRAME_WIDTH  = 840;
    private static final int PRE_FRAME_HEIGHT = 540;
    private static final int MIN_FRAME_WIDTH  = 640;
    private static final int MIN_FRAME_HEIGHT = 480;
    
    private static final int ROW_RESULT_MESSAGES = 160;
    
    // RpcClient �ɐݒ肷�� ErrorResponseHandler
    private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
        // RPC�����������ɔ��������G���[�����o�͂���D
        Log.warning(reason);
        if (response != null) {
            Map<String, Object> error = response.getError();
            Log.warning(
                "error: code: %s, message: %s, data: %s",
                error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
            );
        }
        if (ex != null) {
            Log.warning(ex);
        }
    };
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> RemoteController.launch(args));
    }
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void launch(String[] args)
    {
        try {
            // RemoteController �̃C���X�^���X�𐶐�����D
            RemoteController controller = new RemoteController(args);
            
            // GUI���Z�b�g�A�b�v����D
            controller.setupGui();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final CommandParser parser;
    private final TaskExecutor  executor;
    
    private RpcClient rpcClient = null;
    
    private ServerAddressPanel svrAddrPnl = null;
    private CommandBox         cmdBox     = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void startRemoteControl(String address)
    {
        if (address == null) {
            return;
        }
        // RpcClient �̊J�n������ TaskExecutor �̃X���b�h�Ŏ��s����D
        this.executor.execute(() -> openRpcClient(address));
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void stopRemoteControl()
    {
        // �T�[�o�A�h���X�p�l���̏�Ԃ�ύX����D
        this.svrAddrPnl.setState(ConnectionState.IDLE);
        
        // RpcClient �̒�~������ TaskExecutor �̃X���b�h�Ŏ��s����D
        this.executor.execute(() -> closeRpcClient());
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void setSelectedCommand(String command)
    {
        this.cmdBox.setSelectedCommand(command);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void sendCommand(String command)
    {
        if (this.rpcClient == null) {
            return;
        }
        // ���u�R�}���h��JSON������(RPC�v��)�֕ϊ�����D
        RpcRequest request;
        try {
            request = this.parser.parseCommand(command);
            // InvocationTargetException, IllegalAccessException
        }
        catch (Exception ex) {
            // ��O�����������ꍇ�̓��b�Z�[�W��\�����Ė߂�D
            PopupMessage.warning(this, ex);
            return;
        }
        // RPC�v���� null �̏ꍇ�͖߂�D
        if (request == null) {
            return;
        }
        // ���u�R�}���h��\������D
        Log.message(LINE_SEPARATOR);
        Log.message(LINE_HEADER + command);
        Log.message(LINE_SEPARATOR);
        
        // TaskExecutor �̃X���b�h���� RPC�v�����T�[�o�֑��M����D
        this.executor.execute(() -> send(request));
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    private RemoteController(String[] args)
    {
        // �R�}���h�n���h���𐶐�����D
        this.parser = new CommandParser();
        
        // TaskExecutor �𐶐�����D
        this.executor = new TaskExecutor(
            Executors.newSingleThreadExecutor(), STR_PROGRAM_NAME
        );
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void setupGui()
    {
        // �R�}���h���X�g�p�l���𐶐�����D
        Set<String>      nameSet   = this.parser.getMethodNameSet();
        String[]         cmdArray  = nameSet.toArray(new String[0]);
        CommandListPanel cmdLstPnl = new CommandListPanel(this, cmdArray);
        
        // �T�[�o�A�h���X���͗p�R���|�[�l���g�𐶐�����D
        this.svrAddrPnl = new ServerAddressPanel(this);
        
        // �R�}���h���́E���M�p�R���|�[�l���g�𐶐�����D
        this.cmdBox = new CommandBox(this);
        
        // �o�͗p�R���|�[�l���g�𐶐����CLogPrinter �Ƃ��Đݒ肷��D
        MessagePanel msgPnl = new MessagePanel(ROW_RESULT_MESSAGES);
        msgPnl.setBorder(BDR_EMPTY_8);
        Log.setLogPrinter(msgPnl);
        
        // �R���|�[�l���g�����C�A�E�g����D
        cmdLstPnl.add(this.svrAddrPnl, BorderLayout.NORTH);
        msgPnl.add(this.cmdBox, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(cmdLstPnl, BorderLayout.WEST);
        add(msgPnl, BorderLayout.CENTER);
        
        // �t���[���T�C�Y��ݒ肷��D
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setPreferredSize(new Dimension(PRE_FRAME_WIDTH, PRE_FRAME_HEIGHT));
        setSize(PRE_FRAME_WIDTH, PRE_FRAME_HEIGHT);
        
        // �t���[���̃^�C�g����ݒ肷��D
        setTitle(STR_PROGRAM_NAME);
        
        // L&F ��ݒ肷��D
        AppUtilities.setLookAndFeel(this);
        
        // ���̃t���[���̐����T�C�Y��ݒ肷��D
        pack();
        Dimension curSize = getSize();
        setPreferredSize(curSize);
        
        // �����\���ʒu��ݒ肷��D
        AppUtilities.setLocationToCenter(this);
        
        // �I��������o�^����D
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent ev)
            {
                RemoteController.this.closing();
            }
        });
        // ����Ԃɂ���D
        setVisible(true);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void closing()
    {
        // ���䒆�ł���΁C
        // RpcClient �̒�~���������s����D
        if (this.svrAddrPnl.isControllingState()) {
            stopRemoteControl();
        }
        // TaskExecutor ���V���b�g�_�E������D
        if (this.executor != null) {
            this.executor.shutdownAndAwaitTermination();
        }
        // ���̃t���[���̏I�����������s����D
        dispose();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void openRpcClient(String address)
    {
        int portNum = RPC_PORT_NUMBER;
        try {
            // RpcClient �𐶐�����D
            this.rpcClient = new RpcClient(
                address,	// RPC�T�[�o���܂���IP�A�h���X
                portNum,	// RPC�T�[�o�̃|�[�g�ԍ�
                true,		// JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
                true,		// JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
                HANDLER		// ErrorResponseHandler �̃C���X�^���X
            );	// UnknownHostException
            
            // RpcClient �̏������J�n����D
            this.rpcClient.open();
            // IOException
            
            // GUI�̏����F
            SwingUtilities.invokeLater(() -> {
                // �T�[�o�A�h���X�p�l���̏�Ԃ�ύX����D
                this.svrAddrPnl.setState(ConnectionState.CONTROLLING);
                
                // �t���[���̃^�C�g����ύX����D
                setTitle(String.format(STR_TITLE_FORMAT, address));
            });
        }
        catch (Exception ex) {
            String msg = String.format(
                STR_CANT_CONNECT,
                address, Integer.valueOf(portNum), ex.getMessage()
            );
            PopupMessage.warning(this, msg);
            
            // GUI�̏����F
            SwingUtilities.invokeLater(() -> {
                // �T�[�o�A�h���X�p�l���̏�Ԃ�ύX����D
                this.svrAddrPnl.setState(ConnectionState.IDLE);
            });
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void closeRpcClient()
    {
        // RpcClient �̏������I������D
        if (this.rpcClient != null) {
            this.rpcClient.close();
        }
        // �e�ϐ����N���A���Ă����D
        this.rpcClient = null;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void send(RpcRequest request)
    {
        // RPC�v�����T�[�o�֑��M����D
        Future<RpcResponse> future = this.rpcClient.request(request);
        
        // TaskExecutor �̃X���b�h�� RPC������҂D
        this.executor.execute(() -> receive(future));
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void receive(Future<RpcResponse> future)
    {
        try {
            // RPC������҂D
            future.get(2000, TimeUnit.MILLISECONDS);
            // InterruptedException, ExecutionException, TimeoutException
        }
        catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.warning("%s: %s", ex.getClass().getName(), ex.getMessage());
        }
        catch (Exception ex) {
            Log.error(ex);
        }
    }
}
