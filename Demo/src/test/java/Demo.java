import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @program: taotao
 * @description:
 * @author: 钟兴青
 * @create: 2018-11-20 17:59
 **/
public class Demo {

    @Test
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("05","张五");
        map.put("03","张三");
        map.put("12","张十二");
        map.put("04","张四");
        map.put("06","张六");
        map.put("01","张一");
        map.put("11","张十一");
        map.put("08","张八");
        map.put("15","张十五");
        map.put("09","张九");
        map.put("02","张二");
        map.put("10","张十");
        map.put("14","张十四");
        map.put("13","张十三");
        map.put("07","张七");

//        Set<String> set = map.keySet();//把所有键值存入set集合
//
//        for(Iterator<String> it = set.iterator(); it.hasNext(); )
//        {
//            String stu = it.next();
//            String addr = map.get(stu);
//            System.out.println(stu+".."+addr);
//        }


//        Iterator<String> iterator = set.iterator();
//        while (iterator.hasNext()){
//            Object key = iterator.next();
//            Object o = map.get(key);
//            System.out.println(key+""+o);
//        }

//        Set<Map.Entry<String, String>> entries = map.entrySet();
//
//
//        for (Iterator<Map.Entry<String, String>> iterator = entries.iterator();iterator.hasNext();){
//            Map.Entry<String, String> next = iterator.next();
//            String key = next.getKey();
//            String value = next.getValue();
//            System.out.println(key+value);
//        }


        Set<String> strings = map.keySet();

        for ( Iterator<String> iterator = strings.iterator();iterator.hasNext();){
            String next = iterator.next();
            String s = map.get(next);

            System.out.println(next+""+s);
        }
    }
}
