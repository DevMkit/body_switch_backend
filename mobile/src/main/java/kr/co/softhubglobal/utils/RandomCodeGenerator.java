package kr.co.softhubglobal.utils;

public class RandomCodeGenerator {

    public static String generateCode(int length) {

        String pattern = "ABCDEFGHJKLMNOPQRSTUVWXYZ" + "123456789" + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++ ) {
            int index = (int)(pattern.length() * Math.random());
            stringBuilder.append(
                    pattern.charAt(index)
            );
        }

        return stringBuilder.toString();
    }

    public static String generateCodeOnlyNumber(int length) {

        String pattern = "123456789";
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++ ) {
            int index = (int)(pattern.length() * Math.random());
            stringBuilder.append(
                    pattern.charAt(index)
            );
        }

        return stringBuilder.toString();
    }
}
