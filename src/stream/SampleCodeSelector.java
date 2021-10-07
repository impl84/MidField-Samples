
package stream;

import java.io.IOException;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SampleCodeSelector
 *
 * Date Modified: 2021.09.23
 *
 */

//==============================================================================
public class SampleCodeSelector
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	
	// selectSampleCode ���\�b�h�̌��ʂ������萔
	private static final int
		END_OF_SELECTOR		= -1,
		INDEX_OUT_OF_BOUNDS	= -2;
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================

//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------

	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//	
	public static void main(String[] args)
	{
		SampleCodeSelector selector = null;
		try {
			// SampleCodeSelector �𐶐�����D
			selector = new SampleCodeSelector();
				// SystemException
			
			// �T���v���R�[�h�̎��s�ƏI�����J��Ԃ��D
			selector.mainLoop();
		}
		catch (Exception ex) {
			// SampleCodeSelector �̓��쒆�ɗ�O�����������D
			System.out.println("��SampleCodeSelector �̎��s���ɗ�O���������܂����D");
			ex.printStackTrace();
		}
		finally {
			// SampleCodeSelector ����������Ă���ꍇ�́C�I�����������s����D
			if (selector != null) {
				selector.close();
			}
		}
	}
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================

	//- PRIVATE VARIABLE -------------------------------------------------------
	
	// MidField System
	private final MfsNode mfs;
	
	// ���o�͍\���c�[���𐶐�����D
	private final ConfigTool cfgTool;
	
	// �T���v���R�[�h�Ƃ��Ď��s�\�ȃC���X�^���X�̔z��
	private final AbstractSampleCode[] samples;
	
	// �R���\�[���𗘗p���������̓��o��
	private final ConsoleReader reader;
	private final ConsolePrinter printer;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	SampleCodeSelector()
		throws	SystemException
	{
		// �R���\�[���𗘗p���������̓��o�͂̏���������D
		this.reader = ConsoleReader.getInstance();
		this.printer = ConsolePrinter.getInstance();
		
		this.printer.println("> SampleCodeSelector �̏������J�n���܂��D");
		
		// MidField System �����������C�N������D
		this.printer.println("> MidField System ���N�����܂��D");
		this.mfs = MfsNode.initialize();	// SystemException
		this.mfs.activate();				// SystemException
		
		// ���o�͍\���c�[���𐶐�����D
		this.cfgTool = new ConfigTool(this.reader);
		
		// �T���v���R�[�h�Ƃ��Ď��s�\�ȃC���X�^���X��v�f�Ƃ���z��𐶐�����D
		this.samples = new AbstractSampleCode[] {
			new DeviceToRendererEx(),
			new DeviceToNetworkEx(),
			new NetworkToRendererEx(),
			new NetworkToNetworkEx()
		};
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void mainLoop()
	{
		// �T���v���R�[�h��I�����C�J�n�܂��͒�~���J��Ԃ��D
		while (true) {
			// �T���v���R�[�h�̃��X�g��\������D
			printSampleCodeList();
			
			// �T���v���R�[�h��I������D
			int result = selectSampleCode();
			if (result == END_OF_SELECTOR) {
				// �I���ԍ������͂��ꂽ�̂Ń��[�v�𔲂���D
				break;
			}
			else if (result == INDEX_OUT_OF_BOUNDS) {
				// �͈͊O�̔ԍ������͂��ꂽ�̂Ŏ��̃��[�v�̏����ɓ���D
				continue;
			}
			// ���͂��ꂽ�ԍ��̃T���v���R�[�h�̏������J�n�܂��͏I������D
			changeRunningState(result);
		}
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void close()
	{
		// MidField System ���I������D
		if (MfsNode.isActive()) {
			// �������I�����Ă��Ȃ��T���v���R�[�h������ꍇ�͏I��������D
			closeAllSampleCodes();
			
			this.printer.println("> MidField System ���I�����܂��D");
			this.mfs.shutdown();
		}
		// �W�����͂����1�s�ǂݍ��݂ɗ��p���� ConsoleReader ���������D
		this.reader.release();
		
		this.printer.println("> SampleCodeSelector �̏������I�����܂��D");
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printSampleCodeList()
	{
		// �T���v���R�[�h�̈ꗗ��\������D
		this.printer.println("");
		this.printer.println("���T���v���R�[�h�ꗗ�F");
		
		// �T���v���R�[�h�Ƃ��Ď��s�\�ȃC���X�^���X�̔z��𑖍�����D
		for (int n = 0; n < this.samples.length; n++) {
			// �T���v���R�[�h�̃C���X�^���X���擾����D
			AbstractSampleCode sample = this.samples[n];
			
			// �����Ԃɉ������o�͕���������߂�D
			String runningState = null;
			if (sample.isRunning()) {
				runningState = "���쒆";
			}
			else {
				runningState = "��~��";
			}
			// �T���v���R�[�h�̔ԍ��E��ԁE�������o�͂���D
			this.printer.printf(" [%02d] (%s) %s\n", n,
				runningState,
				sample.getDescription()
			);
		}
	}
		
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private int selectSampleCode()
	{
		int res = 0;
		
		// �T���v���R�[�h��I�����C�J�n/��~����D
		try {
			// �T���v���R�[�h�ԍ����L�[�{�[�h����擾����D
			this.printer.printf("> �ԍ�����[0-%d: ���s�C%d: �I��]�F",
				this.samples.length - 1, this.samples.length
			);
			String line = this.reader.readLine();	// IOException
			int n = Integer.parseInt(line);			// NumberFormatException
			
			// ���͂��ꂽ�l�͈̔͂��m�F����D
			if ((n >= 0) && (n < this.samples.length)) {
				// �I�����ꂽ�T���v���R�[�h�̔z��̃C���f�b�N�X�l��
				// ���U���g�R�[�h�ɐݒ肷��D
				res = n;
			}
			else if (n == this.samples.length) {
				// �I�����Ӗ�����ԍ������͂��ꂽ�̂ŁC
				// ���U���g�R�[�h�� END_OF_SELECTOR ��ݒ肷��D
				this.printer.println("  �v���O�������I�����܂��D");
				res = END_OF_SELECTOR;
			}
			else {
				// ���U���g�R�[�h�� INDEX_OUT_OF_BOUNDS ��ݒ肷��D
				this.printer.println("  ���K�؂Ȕԍ�����͂��Ă��������D");
				res = INDEX_OUT_OF_BOUNDS;
			}
		}
		catch (NumberFormatException ex) {
			// NumberFormatException �����������ꍇ�́C
			// ���U���g�R�[�h�� INDEX_OUT_OF_BOUNDS ��ݒ肷��D
			this.printer.printf("  ���K�؂Ȕԍ�����͂��Ă��������D(%s)\n",
				ex.getMessage()
			);
			res = INDEX_OUT_OF_BOUNDS;
		}
		catch (IOException ex) {
			// IOException �����������ꍇ�́C��O���b�Z�[�W��\�����āC
			// ���U���g�R�[�h�� END_OF_SELECTOR ��ݒ肷��D
			this.printer.println("���L�[�{�[�h����̓��͏����ŗ�O���������܂����D");
			ex.printStackTrace();
			res = END_OF_SELECTOR;
		}
		return res;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void changeRunningState(int idx)
	{
		// �I�����ꂽ�T���v���R�[�h�̃C���X�^���X���擾����D
		AbstractSampleCode selectedSample = this.samples[idx];
		
		// �T���v���R�[�h�̓����Ԃɂ��C
		// �T���v���R�[�h�̏������J�n�܂��͏I������D
		if (selectedSample.isRunning()) {
			// �T���v���R�[�h�̏������I������D
			selectedSample.close();
		}
		else {
			// �T���v���R�[�h�̏������J�n����D
			selectedSample.open(this.cfgTool);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void closeAllSampleCodes()
	{
		// �T���v���R�[�h�Ƃ��Ď��s�\�ȃC���X�^���X�̔z��𑖍�����D
		for (int n = 0; n < this.samples.length; n++) {
			// �T���v���R�[�h�̃C���X�^���X���擾����D
			AbstractSampleCode sample = this.samples[n];
			
			// �������I�����Ă��Ȃ��T���v���R�[�h������ꍇ�͏������I��������D
			if (sample.isRunning()) {
				sample.close();
			}
		}
	}
}