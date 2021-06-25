import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestListOrArray {

  private static String[] testArray(String source) throws Exception {
    Thread.sleep((long) (1000 * Math.random()));
    return new String[0];
  }

  private static String[] testList(List<String> list, String source) throws Exception {
    Thread.sleep((long) (1000 * Math.random()));
    return list.toArray(new String[list.size()]);
  }

  private static String[] testListArray(String source) throws Exception {
    return testList(new ArrayList<String>(), source);
  }

  private static String[] testListLinked(String source) throws Exception {
    return testList(new LinkedList<String>(), source);
  }

  private static int differenceCount = 0;

  private static void printDifference(String onSource) throws Exception {
    var mapTimes = new HashMap<String, Long>();
    var arrayStart = System.currentTimeMillis();
    var arraySize = testArray(onSource).length;
    var arrayTime = System.currentTimeMillis() - arrayStart;
    mapTimes.put("Array     ", arrayTime);
    var listArrayStart = System.currentTimeMillis();
    var listArraySize = testListArray(onSource).length;
    var listArrayTime = System.currentTimeMillis() - listArrayStart;
    mapTimes.put("ListArray ", listArrayTime);
    var listLinkedStart = System.currentTimeMillis();
    var listLinkedSize = testListLinked(onSource).length;
    var listLinkedTime = System.currentTimeMillis() - listLinkedStart;
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
    while (sorted.hasNext()) {
      var entry = sorted.next();
      index++;
      System.out.print(index);
      System.out.print("° - ");
      System.out.print(entry.getKey());
      if (index == 1) {
        firstElapsed = entry.getValue();
        System.out.print(" = ");
        System.out.print(firstElapsed);
        System.out.println(" millis.");
      } else {
        var otherElapsed = entry.getValue() - firstElapsed;
        System.out.print(" + ");
        System.out.print(otherElapsed);
        System.out.println(" millis.");
      }
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Testing between List or Array, wich one is fast.");
    printDifference("hsdjkfg,sdfg,dfgsdgsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgsdsdgsdfg,sdfgsd,sdfgsdfg,sdfg,sdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sasdf,asdf,asfasdfasf,asdfasdf,asfasdf,asdfas,dfasf,dfg,dfgsdgsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgs,sdfgsdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfgsdfgsdfg,sdfgdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgsdsdsdfg,sdfgsdfgs,dfgsdfg,sdfgsd,fgsdfg,sdfgsdfg,sdfgs,dfgsdfg,sdfgsdf,gfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgsd,sdfg,sdfgsdf,gsdfg,sdfgsd,fgsdfgsdfg,sdfgsdfg,sdfgsdf,gsdfg,sdfgsd,fgsdfg,sdfgsdfg,sdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfgs,dfgsd,fgsdfg,sdfgsd,fgsdfg,sdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfgs,dfgsdfg,sdfgs,dfgsdfg,sdfg,sdfgsdfg,sdfgs,dfgsdf,gsdfg,sdfg,dfgsdgsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdsdfg,sdfgsdfg,sdfgsdf,gsdfg,sdgsd,fgsdfgsdfg,sdfgsdfg,.sdfgsdfg,sdfg,sdfgsdfgsdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfgsdfgdfgsdfgsd,fgsdfg,sdfg,sdfgsdfgsd,fgsdfg,gsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgssdfg,sdfgsdfg,sdfgsdfgsdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdf,gsdfg,sdfgsdfg,sdfg,sdfgsdfgsdfg,sdfgsdf,gsdfgsdfgsd,fg,sdfgsdf,gsdfgs,dfgsdfg,sdfgsdf,gsdfg,sdfgsdfgsdfg,sdfgsd,fgsdfgsdfg,sdfgsdf,gsdfgsdfg,sdfgs,dfgsdfgsdf,gsdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfg,sdfgsdfg,sdfgsdf,gsdfg,dgsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdsadf,asdfas,dfasdfasfas,fdasdfasdf,asdfasdfasdfasd,fasdfas,dfasdf,asdfasdfasdf,asdfasdfs,dfasdfa,sdfasdfas,dfasdfasdf,asdfasdf,asdfasdfasdf,asdfasd,fasdfas,dfasdf,asdfasdfasdf,as,dfas,dfasdf,asdf,asdfasdf,asdf,asdf,asdfasd,fasdfa,sdfasdf,asdfasdf,asdf,asdfas,dfasdf,asdfasdf,asdf,asdf,asdfasdf,asdf,asdfasd,fasdf,asdfasdf,asdfas,dfasdf,asdf,asdfas,dfasdf,asdfas,df,asdf,sadf,asdfa,sdfasdf,asdfasd,fasdf,gsdsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfv");
    printDifference(
        "hsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfvhsdjkfg,sdfg,dfgsdgs,dfgsd,dsf,gdfsgdsfg,dffgsd,fgsdfgsdfg,sdgsdg,sdfgsdfg,sdfgsdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfg,sdfgdsfg,sdfgsdfg,dsfgdsfg,dsfgdsfg,sdfg,sdfgsdfg,sdfgsdfg,sdfgsdfgsdf,gsdfgsdf,gsdfgsd,fgsdfg,sdfg,sdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,sdfgsdfg,sdfg,sdfg,sdfgsdfg,dsfg,sdfg,dsfgsdf,gsdfg,sdfgsdfgsdf,gsdf,gdfsg,sdfgsdfg,sdfg,sdfgsdfgsdf,gsdfg,sdfg,dsfgdfsg,dsfg,sdfg,sdfgsdf,gdsfg,sdfgsd,fgdsf,gsdf,gsfd,gsdfg,sddf,gsdfg,sdfg,sdfgsd,fgsd,fgsdg,sdfg,sdfgsdfg,sdfgsdfgs,dfgsdfg,sdfgs,dfgsdf,gsdfgsd,fgsdfg,sdfg,sdfgsd,fgsdfg,sdfgsdfg,dsdfg,qwerqwer,asvadvsdfv");
        System.out.println("----------------------------------------");
  }

}
