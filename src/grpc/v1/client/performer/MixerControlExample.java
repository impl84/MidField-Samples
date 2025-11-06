
package grpc.v1.client.performer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.MixerControlClient;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;
import grpc.v1.client.Reporter;
import util.SimpleViewer;

public class MixerControlExample
    extends
        ExampleBase
{
    private static final String MIXER_NAME = "Experimental Mixer";
    
    public MixerControlExample(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("MixerToRenderer");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureMixerSource(instanceId, MIXER_NAME);
        performerIo.configureRenderer(instanceId);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.openAndStart(instanceId);
        
        var mixerControl = new MixerControlClient(getManagedChannel());
        var controlView  = new MixerControlView(MIXER_NAME, instanceId, mixerControl);
        
        Reporter.readLine("> Enter キーの入力を待ちます．");
        
        controlView.close();
        mixerControl.close();
        
        performerControl.stopAndClose(instanceId);
    }
}

class MixerControlView
    implements
        MouseListener
{
    private final String             instanceId;
    private final MixerControlClient mixerControl;
    
    private final SimpleViewer       viewer;

    MixerControlView(String title, String instanceId, MixerControlClient mixerControl)
    {
        this.instanceId   = instanceId;
        this.mixerControl = mixerControl;

        this.viewer = new SimpleViewer(title);
        var container = this.viewer.getContentPane();
        container.addMouseListener(this);

        this.viewer.validate();
        this.viewer.setVisible(true);
    }
    
    public void close()
    {
        this.viewer.dispose();
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        this.mixerControl.streamUiEvent(
            instanceId,
            com.midfield_system.grpc.v1.MouseEvent.Action.CLICK,
            e.getX(),
            e.getY()
        );
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        this.mixerControl.streamUiEvent(
            instanceId,
            com.midfield_system.grpc.v1.MouseEvent.Action.DOWN,
            e.getX(),
            e.getY()
        );
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.mixerControl.streamUiEvent(
            instanceId,
            com.midfield_system.grpc.v1.MouseEvent.Action.UP,
            e.getX(),
            e.getY()
        );
    }
}