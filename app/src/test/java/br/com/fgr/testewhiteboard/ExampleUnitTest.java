package br.com.fgr.testewhiteboard;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.fgr.testewhiteboard.model.DBHelper;
import br.com.fgr.testewhiteboard.model.GenerateHashCode;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dadoUmaStringRetornaUmInteger() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String str = GenerateHashCode.hashCode("Algum texto aqui");

        System.out.println(DBHelper.getIntegerId(str));

    }

}