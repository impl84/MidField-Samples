
package grpc.v1.client;

import java.util.Iterator;

import com.midfield_system.grpc.v1.DisableControlRequest;
import com.midfield_system.grpc.v1.EnableControlRequest;
import com.midfield_system.grpc.v1.NodeControlGrpc;
import com.midfield_system.grpc.v1.NodeControlGrpc.NodeControlBlockingStub;
import com.midfield_system.grpc.v1.NodeEventNotification;
import com.midfield_system.grpc.v1.NodeEventRequest;

/**
 * NodeControlExampleは、gRPC経由でノード制御の有効化/無効化や
 * イベント購読・受信を行うクライアントサンプルです。
 * ExampleBaseを継承し、execute()で一連の操作を実行します。
 */
class NodeControlExample
    extends
        ExampleBase
{
    /** gRPCのNodeControlサービス用BlockingStub（同期呼び出し用） */
    private final NodeControlBlockingStub nodeControl;
    
    /**
     * コンストラクタ
     * 
     * @param serverAddress
     *        サーバーアドレス
     * @param portNumber
     *        ポート番号
     */
    NodeControlExample(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        // gRPCチャンネルからBlockingStubを生成
        this.nodeControl = NodeControlGrpc.newBlockingStub(getManagedChannel());
    }
    
    /**
     * 一連のノード制御・イベント購読・解除処理を実行する。
     * 1. 制御有効化リクエスト送信
     * 2. イベント購読開始（別スレッドで受信処理）
     * 3. ユーザーのEnter入力待ち
     * 4. 制御無効化リクエスト送信
     */
    @Override
    public void execute()
    {
        // 1. ノード制御有効化リクエスト送信
        var enableControlResponse = this.nodeControl.enableControl(
            EnableControlRequest.newBuilder()
                .build()
        );
        Reporter.message("EnableControlResponse: " + enableControlResponse.getSuccess());
        
        // 2. イベント購読開始（Iteratorでストリーム受信）
        var iterator = this.nodeControl.subscribeNodeEvent(
            NodeEventRequest.newBuilder()
                .build()
        );
        // イベント受信処理は別スレッドで実行
        new Thread(() -> handleNodeEvent(iterator)).start();
        
        // 3. ユーザーのEnter入力待ち（イベント受信中）
        Reporter.waitForEnter();
        
        // 4. ノード制御無効化リクエスト送信
        var disableControlResponse = this.nodeControl.disableControl(
            DisableControlRequest.newBuilder()
                .build()
        );
        Reporter
            .message("DisableControlResponse: " + disableControlResponse.getSuccess());
    }
    
    /**
     * イベントストリームを受信し、各種イベントをReporter経由で表示する。
     * 
     * @param iterator
     *        NodeEventNotificationのストリームイテレータ
     */
    private void handleNodeEvent(Iterator<NodeEventNotification> iterator)
    {
        while (iterator.hasNext()) {
            var response = iterator.next();
            
            // イベント種別ごとにメッセージを表示
            switch (response.getEventTypeCase()) {
            case LOG_MESSAGE_EVENT:
                // ログメッセージイベント
                Reporter.message(response.getLogMessageEvent().getEventMessage());
                break;
            case NODE_METRICS_EVENT:
                // ノードメトリクス（リソース状態）イベント
                Reporter.message(response.getNodeMetricsEvent().getEventMessage());
                break;
            case NODE_EXCEPTION_EVENT:
                // ノード例外イベント
                Reporter.message(response.getNodeExceptionEvent().getEventMessage());
                break;
            case EVENTTYPE_NOT_SET:
                // イベント種別未設定
                Reporter.message(response.getEventTypeCase().toString());
                break;
            }
        }
    }
}
