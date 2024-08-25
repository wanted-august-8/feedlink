package com.feedlink.api.feedlink_api.domain.member.util;

import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 비밀번호를 검증하는 유틸리티 클래스입니다.
 * 검증 조건은 다음과 같습니다.
 * - 다른 개인 정보와 유사한 비밀번호는 사용할 수 없습니다.: 사용자 이름, 이메일 등과 유사한 비밀번호를 사용하지 않도록 합니다.
 * - 비밀번호는 최소 10자 이상이어야 합니다.: 비밀번호의 길이를 확인합니다.
 * - 통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다.: 일반적인 비밀번호 목록과 비교합니다.
 * - 숫자로만 이루어진 비밀번호는 사용할 수 없습니다.: 비밀번호가 숫자로만 이루어져 있는지 확인합니다.
 * - 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.: 비밀번호가 최소 두 종류의 문자(숫자, 문자, 특수문자)를 포함하는지 확인합니다.
 * - 이전 비밀번호와 동일하게 설정할 수 없습니다.: 이전 비밀번호와 동일한지 확인합니다.
 * - 3회 이상 연속되는 문자 사용이 불가합니다.: 연속되는 문자가 3회 이상 포함되는지 확인합니다.
 */
public class PasswordValidator {

    // 자주 사용되는 비밀번호 목록 예시
    private static final List<String> COMMON_PASSWORDS = Arrays.asList("a123456", "password123", "qwer123", "abc123");

    /**
     * 비밀번호가 모든 정책을 만족하는지 검증합니다.
     * 정책을 위반할 경우, 해당하는 CustomException을 던집니다.
     *
     * @param password         현재 설정하려는 비밀번호
     * @param previousPassword 이전 비밀번호 (변경 시)
     * @param username         사용자 이름
     * @param email            사용자 이메일
     */
    public static void validatePassword(String password, String previousPassword, String username, String email) {
        if (password.length() < 10) {
            throw new CustomException(ErrorCode.PASSWORD_TOO_SHORT);
        }

        if (isSimilarToPersonalInfo(password, username, email)) {
            throw new CustomException(ErrorCode.PASSWORD_SIMILAR_TO_PERSONAL_INFO);
        }

        if (isCommonPassword(password)) {
            throw new CustomException(ErrorCode.PASSWORD_HAS_SEQUENTIAL_CHARS);
        }

        if (!containsAtLeastTwoCharTypes(password)) {
            throw new CustomException(ErrorCode.PASSWORD_LACKS_VARIETY);
        }

        if (password.equals(previousPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_PREVIOUS);
        }

        if (hasSequentialChars(password)) {
            throw new CustomException(ErrorCode.PASSWORD_HAS_SEQUENTIAL_CHARS);
        }
    }

    /**
     * 비밀번호가 개인 정보(사용자 이름 또는 이메일)와 유사한지 확인합니다.
     *
     * @param password 비밀번호
     * @param username 사용자 이름
     * @param email    사용자 이메일
     * @return 비밀번호가 사용자 이름 또는 이메일의 로컬 부분과 유사한 경우 true 반환
     */
    private static boolean isSimilarToPersonalInfo(String password, String username, String email) {
        return password.toLowerCase().contains(username.toLowerCase()) || password.toLowerCase().contains(email.split("@")[0].toLowerCase());
    }


    /**
     * 비밀번호가 자주 사용되는 비밀번호 목록에 포함되는지 확인합니다.
     *
     * @param password 비밀번호
     * @return 비밀번호가 자주 사용되는 비밀번호 목록에 있는 경우 true 반환
     */
    private static boolean isCommonPassword(String password) {
        return COMMON_PASSWORDS.contains(password.toLowerCase());
    }

    /**
     * 비밀번호가 숫자, 문자, 특수문자 중 최소 두 가지 유형을 포함하는지 확인합니다.
     *
     * @param password 비밀번호
     * @return 비밀번호가 최소 두 가지 유형의 문자를 포함하는 경우 true 반환
     */
    private static boolean containsAtLeastTwoCharTypes(String password) {
        boolean hasLetter = Pattern.compile("[A-Za-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("\\d").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();

        int count = 0;
        if (hasLetter) count++;
        if (hasDigit) count++;
        if (hasSpecial) count++;

        return count >= 2;
    }

    /**
     * 비밀번호에 3회 이상 연속되는 문자가 있는지 확인합니다.
     *
     * @param password 비밀번호
     * @return 비밀번호에 3회 이상 연속되는 문자가 있으면 true 반환
     */
    private static boolean hasSequentialChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}