
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
    
    // RpcClient に設定する ErrorResponseHandler
    private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
        // RPC応答処理時に発生したエラー情報を出力する．
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
            // RemoteController のインスタンスを生成する．
            RemoteController controller = new RemoteController(args);
            
            // GUIをセットアップする．
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
        // RpcClient の開始処理を TaskExecutor のスレッドで実行する．
        this.executor.execute(() -> openRpcClient(address));
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void stopRemoteControl()
    {
        // サーバアドレスパネルの状態を変更する．
        this.svrAddrPnl.setState(ConnectionState.IDLE);
        
        // RpcClient の停止処理を TaskExecutor のスレッドで実行する．
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
        // 遠隔コマンドをJSON文字列(RPC要求)へ変換する．
        RpcRequest request;
        try {
            request = this.parser.parseCommand(command);
            // InvocationTargetException, IllegalAccessException
        }
        catch (Exception ex) {
            // 例外が発生した場合はメッセージを表示して戻る．
            PopupMessage.warning(this, ex);
            return;
        }
        // RPC要求が null の場合は戻る．
        if (request == null) {
            return;
        }
        // 遠隔コマンドを表示する．
        Log.message(LINE_SEPARATOR);
        Log.message(LINE_HEADER + command);
        Log.message(LINE_SEPARATOR);
        
        // TaskExecutor のスレッドから RPC要求をサーバへ送信する．
        this.executor.execute(() -> send(request));
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    private RemoteController(String[] args)
    {
        // コマンドハンドラを生成する．
        this.parser = new CommandParser();
        
        // TaskExecutor を生成する．
        this.executor = new TaskExecutor(
            Executors.newSingleThreadExecutor(), STR_PROGRAM_NAME
        );
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void setupGui()
    {
        // コマンドリストパネルを生成する．
        Set<String>      nameSet   = this.parser.getMethodNameSet();
        String[]         cmdArray  = nameSet.toArray(new String[0]);
        CommandListPanel cmdLstPnl = new CommandListPanel(this, cmdArray);
        
        // サーバアドレス入力用コンポーネントを生成する．
        this.svrAddrPnl = new ServerAddressPanel(this);
        
        // コマンド入力・送信用コンポーネントを生成する．
        this.cmdBox = new CommandBox(this);
        
        // 出力用コンポーネントを生成し，LogPrinter として設定する．
        MessagePanel msgPnl = new MessagePanel(ROW_RESULT_MESSAGES);
        msgPnl.setBorder(BDR_EMPTY_8);
        Log.setLogPrinter(msgPnl);
        
        // コンポーネントをレイアウトする．
        cmdLstPnl.add(this.svrAddrPnl, BorderLayout.NORTH);
        msgPnl.add(this.cmdBox, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(cmdLstPnl, BorderLayout.WEST);
        add(msgPnl, BorderLayout.CENTER);
        
        // フレームサイズを設定する．
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setPreferredSize(new Dimension(PRE_FRAME_WIDTH, PRE_FRAME_HEIGHT));
        setSize(PRE_FRAME_WIDTH, PRE_FRAME_HEIGHT);
        
        // フレームのタイトルを設定する．
        setTitle(STR_PROGRAM_NAME);
        
        // L&F を設定する．
        AppUtilities.setLookAndFeel(this);
        
        // このフレームの推奨サイズを設定する．
        pack();
        Dimension curSize = getSize();
        setPreferredSize(curSize);
        
        // 初期表示位置を設定する．
        AppUtilities.setLocationToCenter(this);
        
        // 終了処理を登録する．
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent ev)
            {
                RemoteController.this.closing();
            }
        });
        // 可視状態にする．
        setVisible(true);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void closing()
    {
        // 制御中であれば，
        // RpcClient の停止処理を実行する．
        if (this.svrAddrPnl.isControllingState()) {
            stopRemoteControl();
        }
        // TaskExecutor をシャットダウンする．
        if (this.executor != null) {
            this.executor.shutdownAndAwaitTermination();
        }
        // このフレームの終了処理を実行する．
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
            // RpcClient を生成する．
            this.rpcClient = new RpcClient(
                address,	// RPCサーバ名またはIPアドレス
                portNum,	// RPCサーバのポート番号
                true,		// JSONオブジェクト(文字列)を整形するか否か
                true,		// JSONオブジェクト(文字列)をログ出力するか否か
                HANDLER		// ErrorResponseHandler のインスタンス
            );	// UnknownHostException
            
            // RpcClient の処理を開始する．
            this.rpcClient.open();
            // IOException
            
            // GUIの処理：
            SwingUtilities.invokeLater(() -> {
                // サーバアドレスパネルの状態を変更する．
                this.svrAddrPnl.setState(ConnectionState.CONTROLLING);
                
                // フレームのタイトルを変更する．
                setTitle(String.format(STR_TITLE_FORMAT, address));
            });
        }
        catch (Exception ex) {
            String msg = String.format(
                STR_CANT_CONNECT,
                address, Integer.valueOf(portNum), ex.getMessage()
            );
            PopupMessage.warning(this, msg);
            
            // GUIの処理：
            SwingUtilities.invokeLater(() -> {
                // サーバアドレスパネルの状態を変更する．
                this.svrAddrPnl.setState(ConnectionState.IDLE);
            });
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void closeRpcClient()
    {
        // RpcClient の処理を終了する．
        if (this.rpcClient != null) {
            this.rpcClient.close();
        }
        // 各変数をクリアしておく．
        this.rpcClient = null;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void send(RpcRequest request)
    {
        // RPC要求をサーバへ送信する．
        Future<RpcResponse> future = this.rpcClient.request(request);
        
        // TaskExecutor のスレッドで RPC応答を待つ．
        this.executor.execute(() -> receive(future));
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void receive(Future<RpcResponse> future)
    {
        try {
            // RPC応答を待つ．
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
