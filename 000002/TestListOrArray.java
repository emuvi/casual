import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestListOrArray {

  private static String[] testArray(String source) throws Exception {
    var index = 0;
    var array = new String[10];
    var builder = new StringBuilder();
    for (int i = 0; i < source.length(); i++) {
      var actual = source.charAt(i);
      if (actual == ',') {
        if (index >= array.length) {
          array = Arrays.copyOf(array, array.length + 10);
        }
        array[index] = builder.toString();
        builder = new StringBuilder();
        index++;
      } else {
        builder.append(actual);
      }
    }
    if (index >= array.length) {
      array = Arrays.copyOf(array, array.length + 10);
    }
    array[index] = builder.toString();
    return array.length == index - 1 ? array : Arrays.copyOf(array, index + 1);
  }

  private static String[] testList(List<String> list, String source) throws Exception {
    var builder = new StringBuilder();
    for (int i = 0; i < source.length(); i++) {
      var actual = source.charAt(i);
      if (actual == ',') {
        list.add(builder.toString());
        builder = new StringBuilder();
      } else {
        builder.append(actual);
      }
    }
    list.add(builder.toString());
    return list.toArray(new String[list.size()]);
  }

  private static String[] testListArray(String source) throws Exception {
    return testList(new ArrayList<String>(), source);
  }

  private static String[] testListLinked(String source) throws Exception {
    return testList(new LinkedList<String>(), source);
  }

  private static int differenceCount = 0;

  private static String getWinner(String onSource) throws Exception {
    var mapTimes = new HashMap<String, Long>();
    var arrayStart = System.nanoTime();
    var arraySize = testArray(onSource).length;
    var arrayTime = System.nanoTime() - arrayStart;
    mapTimes.put("Array     ", arrayTime);
    var listArrayStart = System.nanoTime();
    var listArraySize = testListArray(onSource).length;
    var listArrayTime = System.nanoTime() - listArrayStart;
    mapTimes.put("ListArray ", listArrayTime);
    var listLinkedStart = System.nanoTime();
    var listLinkedSize = testListLinked(onSource).length;
    var listLinkedTime = System.nanoTime() - listLinkedStart;
    mapTimes.put("ListLinked", listLinkedTime);
    if (arraySize != listArraySize || arraySize != listLinkedSize) {
      throw new Exception("Array size and List size are different on '" + onSource + "'");
    }
    var sorted = mapTimes.entrySet().stream().sorted(Map.Entry.comparingByValue())
        .iterator();
    System.out.println("----------------------------------------");
    differenceCount++;
    System.out.println("In example: " + differenceCount);
    int index = 0;
    long firstElapsed = 0;
    var result = "";
    while (sorted.hasNext()) {
      var entry = sorted.next();
      index++;
      System.out.print(index);
      System.out.print("° - ");
      System.out.print(entry.getKey());
      if (index == 1) {
        result = entry.getKey();
        firstElapsed = entry.getValue();
        System.out.print(" = ");
        System.out.print(firstElapsed);
        System.out.println(" nanos.");
      } else {
        var otherElapsed = entry.getValue() - firstElapsed;
        System.out.print(" + ");
        System.out.print(otherElapsed);
        System.out.println(" nanos.");
      }
    }
    return result;
  }

  private static String makeField() {
    var result = new StringBuilder();
    int size = 3 + (int) (Math.random() * 50);
    for (int c = 0; c < size; c++) {
      result.append((char) (40 + (int) (Math.random() * 10)));
    }
    return result.toString();
  }

  private static String makeSource() {
    var result = new StringBuilder();
    int fields = 3 + (int) (Math.random() * 100);
    for (int f = 0; f < fields; f++) {
      if (f > 0) {
        result.append(",");
      }
      result.append(makeField());
    }
    return result.toString();
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Testing between List or Array, which one is fast.");
    var winners = new HashMap<String, Integer>();
    for (int i = 0; i < 100_000; i++) {
      var winner = getWinner(makeSource());
      winners.compute(winner, (k, v) -> (v == null) ? 1 : v + 1);
    }
    System.out.println("----------------------------------------");
    winners.forEach((k, v) -> System.out.println(k + " = " + v));
  }

}
