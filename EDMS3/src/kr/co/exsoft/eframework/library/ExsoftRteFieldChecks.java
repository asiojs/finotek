package kr.co.exsoft.eframework.library;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.FieldChecks;

/**
 * validation rule을 추가로 제공한다. 실제 validation check는 RteGenericValidator에 위임한다.
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
@SuppressWarnings("serial")
public class ExsoftRteFieldChecks extends FieldChecks {

	/**
	 * 주민등록번호 유효성 체크
	 * 
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @return boolean
	 */
	public static boolean validateIhIdNum(Object bean, ValidatorAction va, Field field, Errors errors) {


		String ihidnum = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.isValidIdIhNum(ihidnum)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 한글여부 체크
	 * 
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @return boolean
	 */
	public static boolean validateKorean(Object bean, ValidatorAction va, Field field, Errors errors) {

		String value = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.isKorean(value)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 영어여부 체크
	 * 
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @return boolean
	 */
	public static boolean validateEnglish(Object bean, ValidatorAction va, Field field, Errors errors) {

		String value = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.isEnglish(value)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		} else {
			return true;
		}
	}


	/**
	 * HTML tag 포함여부 체크
	 * 
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @return boolean
	 */
	public static boolean validateHtmlTag(Object bean, ValidatorAction va, Field field, Errors errors) {

		String value = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.isHtmlTag(value)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		} else {
			return true;
		}
	}


    /**
     * 패스워드 점검 : 8~20자 이내
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @return boolean
     */ 
	public static boolean validatePassword1(Object bean, ValidatorAction va, Field field, Errors errors) {

		String password = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.checkLength(password)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		}


		return true;
	}
    
    /**
     * 패스워드 점검 : 한글,특수문자,띄어쓰기는 안됨
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @return boolean
     */
	public static boolean validatePassword2(Object bean, ValidatorAction va, Field field, Errors errors) {
		
		String password = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.checkCharacterType(password)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		}


		return true;
	}
    
    /**
     * 패스워드 점검 : 연속된 문자나 순차적인 문자 4개이상 사용금지
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @return boolean
     */
	public static boolean validatePassword3(Object bean, ValidatorAction va, Field field, Errors errors) {
		
		String password = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.checkSeries(password)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		}


	return true;
    }
    
    /**
     * 패스워드 점검 : 반복문자나 숫자 연속 4개이상 사용금지
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @return boolean
     */
	public static boolean validatePassword4(Object bean, ValidatorAction va, Field field, Errors errors) {
		
		String password = FieldChecks.extractValue(bean, field);

		if (!RteGenericValidator.checkSeries(password)) {
			FieldChecks.rejectValue(errors, field, va);
			return false;
		}


		return true;
	}

}
