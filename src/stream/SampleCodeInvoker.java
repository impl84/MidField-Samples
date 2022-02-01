
package stream;

import java.io.IOException;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;

import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: SampleCodeInvoker
 *
 * Date Modified: 2021.11.05
 *
 */
public class SampleCodeInvoker
{
	// - PRIVATE CONSTANT VALUE ------------------------------------------------
	
	// �T���v���R�[�h�̃N���X���̃f�t�H���g�l
	private static final String SAMPLE_CLASS_NAME = "stream.DeviceToRendererEx";
	
// =============================================================================
//  CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
	
	// - PUBLIC STATIC METHOD --------------------------------------------------
	//
	public static void main(String[] args)
	{
		// �T���v���R�[�h�̃N���X�������߂�D
		// ������ 1�ȏ�L��ꍇ�́C�ŏ��̈������T���v���R�[�h�̃N���X���Ƃ���D
		// �����ŗ^�����Ă��Ȃ��ꍇ�́C�f�t�H���g�l���g���D
		String sampleClassName = SAMPLE_CLASS_NAME;
		if (args.length > 0) {
			sampleClassName = args[0];
		}
		SampleCodeInvoker invoker = null;
		try {
			// SampleCodeInvoker �𐶐����C�T���v���R�[�h�̏������J�n����D
			invoker = new SampleCodeInvoker(sampleClassName);
				// SystemException
			
			// �T���v���R�[�h�̏I���̃^�C�~���O��҂D
			invoker.waitForWindowClose();
				// IOException
		}
		catch (Exception ex) {
			// SampleCodeInvoker �̎��s���ɗ�O�����������D
			System.out.println("��SampleCodeInvoker �̎��s���ɗ�O���������܂����D");
			ex.printStackTrace();
		}
		finally {
			// SampleCodeInvoker ����������Ă���ꍇ�́C�I�����������s����D
			if (invoker != null) {
				invoker.close();
			}
		}
	}
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------
	
	// MidField System
	private final MfsNode mfs;
	
	// �T���v���R�[�h
	private final AbstractSampleCode sample;
	
	// �R���\�[���𗘗p�����s�P�ʂ̕�����̓��o��
	private final ConsoleReader reader;
	private final ConsolePrinter printer;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================
	
// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	SampleCodeInvoker(String sampleClassName)
		throws	SystemException
	{
		// �R���\�[���𗘗p���������̓��o�͂̏���������D
		this.reader = ConsoleReader.getInstance();
		this.printer = ConsolePrinter.getInstance();
		
		this.printer.printf("> SampleCodeInvoker �̏������J�n���܂��D(%s)\n",
			sampleClassName
		);
		// �T���v���R�[�h�̃C���X�^���X�𐶐����C�ێ�����D
		this.sample = newSampleCode(sampleClassName);
		
		// MidField System �����������C�N������D
		this.printer.println("> MidField System ���N�����܂��D");
		this.mfs = MfsNode.initialize();	// SystemException
		this.mfs.activate();				// SystemException
		
		// ���o�͍\���c�[���𐶐�����D
		ConfigTool cfgTool = new ConfigTool(this.reader, this.printer);
		
		// �T���v���R�[�h�̏������J�n����D
		this.printer.println("> �T���v���R�[�h�̏������J�n���܂��D");
		this.sample.open(cfgTool);
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void waitForWindowClose()
		throws	IOException
	{
		while (this.sample.isRunning()) {
			this.printer.println("> �E�B���h�E�������� Enter �L�[���������܂ő҂��܂��D");
			this.reader.readLine();
				// IOException
		}
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void close()
	{
		// MidField System ���I������D
		if (MfsNode.isActive()) {
			this.printer.println("> MidField System ���I�����܂��D");
			this.mfs.shutdown();
		}
		// �W�����͂����1�s�ǂݍ��݂ɗ��p���� ConsoleReader ���������D
		this.reader.release();
		
		this.printer.println("> SampleCodeInvoker �̏������I�����܂��D");
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private AbstractSampleCode newSampleCode(String sampleClassName)
	{
		AbstractSampleCode sample = null;
		
		// �T���v���R�[�h�̃C���X�^���X�𐶐�����D
		try {
			// ��������\���̂����O�͈ȉ��̒ʂ�D
			//  forName():
			//		ClassNotFoundException
			//  getDeclaredConstructor():
			//		NoSuchMethodException
			//  newInstance():
			//		InstantiationException, 
			//		IllegalAccessException,
			//		InvocationTargetException
			sample = (AbstractSampleCode)Class.forName(sampleClassName)
				.getDeclaredConstructor()
				.newInstance();
		}
		catch (Exception ex) {
			// ��L�e��̗�O����������\�����������C���̃��\�b�h�Ƃ��ẮC
			// �s���Ȉ����C�܂��͕s�K�؂Ȉ������n���ꂽ���Ƃ�
			// �Ăяo�����Ɏ����ׂ��Ȃ̂ŁC
			// ����������O�������Ƃ��� IllegalArgumentException �𐶐����āC
			// ����𓊂���D
			throw new IllegalArgumentException(ex);
		}
		// �T���v���R�[�h�̃C���X�^���X��Ԃ��D
		return sample;
	}
}
