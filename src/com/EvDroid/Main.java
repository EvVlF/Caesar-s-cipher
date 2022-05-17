package com.EvDroid;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

  static final String cipherText =
      "Еъёчхф Вхзёюлх, адздёиу ф ждэщхб, црбх еёдюэчъщъгюъв южаижжзчх, ждчъёнъгжзчдв. "
          + "Ъы зёюивй жёхчгюв бюнс ж ъы вдгивъгзхбсгрв аёхкдв. Зъеъёс вгъ дмъчющгд, мзд гъюэцъьгджзс тздшд аёхкх "
          + "фчбфъзжф жбъщжзчюъв гъждчъёнъгжзчх мъбдчъмъжадшд югщючющиивх. Ф юэимюб чхни южздёюу ю чгыж юэвъгъгюф, "
          + "здмгъъ дзёхэюч еджздфггиу юэвъгмючджзс мъбдчъмъжаюк едёдадч. Ю зъв гъ въгъъ, еджбъщдчхбх гъищхмх. "
          + "Ф еёюнъб а чрчдщи, мзд чюгдя чжъви вдя югзъббъаз, х чдэвдьгд, вды мёъэвъёгдъ жзёъвбъгюъ ад чжъви шхёвдгюмгдви. "
          + "Гхязю ёънъгюъ вгъ едвдшбх еёдшёхввх югзиюзючгдшд зюех, жеълюхбсгд ждэщхггхф щбф юэимъгюф деёъщъбъггрк жздёдг мъбдчъмъжадя щиню. "
          + "Въгф вдьгд гхэчхзс дзлдв Вхзёюлр, х ъы, цъэ еёъичъбюмъгюф, вхзъёсу.";

  static final char[] alphabetRU =
      new char[] {
        'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р',
        'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
      };

  public static void main(String[] args) {
    StringBuilder longestWord = new StringBuilder(findLongestWordInCipherText(cipherText));
    int shiftValue = findDigitalShiftInLongestCipherWord(longestWord);
    StringBuilder decipherText = decipherAllText(cipherText, shiftValue);
    System.out.println(decipherText);
  }

  public static @NotNull StringBuilder findLongestWordInCipherText(String cipherText) {
    final int longestWordStartFromNotLessThan = 10;
    int longestWordFoundLength = longestWordStartFromNotLessThan;
    StringBuilder longestWord = new StringBuilder(" ");
    Pattern patternFindLongestWord =
        Pattern.compile("([А-яЁё]+){" + longestWordStartFromNotLessThan + "}");
    Matcher matcherInCipherText = patternFindLongestWord.matcher(cipherText);
    for (; !matcherInCipherText.find(); longestWordFoundLength--) {}
    while (matcherInCipherText.find()) {
      if ((matcherInCipherText.end() - matcherInCipherText.start()) > longestWordFoundLength) {
        longestWordFoundLength = matcherInCipherText.end() - matcherInCipherText.start();
        longestWord.replace(0, longestWord.length(), matcherInCipherText.group());
      }
    }
    return longestWord;
  }

  public static @NotNull StringBuilder decipherCharAlgorithm(
      @NotNull StringBuilder cipherText, int shiftValue) {
    StringBuilder decipherText = new StringBuilder("");
    int alphabetPosition;
    int shiftAlphabetPosition = 0;
    char oneCharFromCipherWord = ' ';
    char shiftCharOfDecipherWord = ' ';
    for (int i = 0; i < cipherText.length(); i++) {
      alphabetPosition = 0;
      oneCharFromCipherWord =
          Character.isUpperCase(cipherText.charAt(i))
              ? Character.toLowerCase(cipherText.charAt(i))
              : cipherText.charAt(i);
      for (int j = 0; j < alphabetRU.length - 1; j++) {
        alphabetPosition++;
        if (oneCharFromCipherWord == alphabetRU[j]) {
          break;
        }
      }
      shiftCharOfDecipherWord =
          alphabetPosition + shiftValue > 32
              ? alphabetRU[alphabetPosition + shiftValue - 32 - 1]
              : alphabetRU[alphabetPosition + shiftValue];
      decipherText =
          Character.isUpperCase(cipherText.charAt(i))
              ? decipherText.append(Character.toUpperCase(shiftCharOfDecipherWord))
              : decipherText.append(shiftCharOfDecipherWord);
    }
    return decipherText;
  }

  public static int findDigitalShiftInLongestCipherWord(StringBuilder longestWord) {
    StringBuilder shiftLongestWord = new StringBuilder("");
    final String userKeyConfirm = "yes";
    Scanner userInput = new Scanner(System.in);
    String confirm = "";
    int shiftValue = 0;
    for (shiftValue = 0; !userKeyConfirm.equals(confirm); shiftValue++) {
      shiftLongestWord = new StringBuilder("");
      shiftLongestWord = decipherCharAlgorithm(longestWord, shiftValue);
      System.out.println(shiftLongestWord);
      System.out.println(
          "Слово понятно? Если да - введите "
              + userKeyConfirm
              + " и нажмите Enter. Если нет - просто нажмите Enter");
      confirm = userInput.nextLine();
    }
    return shiftValue;
  }

  public static @NotNull StringBuilder decipherAllText(@NotNull String cipherText, int shiftValue) {
    StringBuilder decipherText = new StringBuilder("");
    Pattern patternWordsAndOthersAsGroups =
        Pattern.compile("(?<words>[А-яЕё]+)(?<others>[^А-яЕё]+)");
    Matcher matcherWordsAndOthersAsGroups = patternWordsAndOthersAsGroups.matcher(cipherText);
    while (matcherWordsAndOthersAsGroups.find()) {
      decipherText.append(
          decipherCharAlgorithm(
              new StringBuilder(matcherWordsAndOthersAsGroups.group("words")), shiftValue - 1));
      decipherText.append(matcherWordsAndOthersAsGroups.group("others"));
    }
    return decipherText;
  }
}
