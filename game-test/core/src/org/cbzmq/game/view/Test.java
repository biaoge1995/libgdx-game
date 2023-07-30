package org.cbzmq.game.view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName Test
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/28 4:07 下午
 * @Version 1.0
 **/
public class Test {
    public static final String sql =
            "select\tcase when \"month\" in (${agg_list}) then t6.month else '' end AS `month`,\n" +
            "\tcase when \"record_date\" in (${agg_list}) then t1.record_date else '' end as record_date,\n" +
            "\tcase when \"seller_id\" in (${agg_list}) then t1.seller_id else '' end as seller_id,\n" +
            "\tcase when \"store_name\" in (${agg_list}) then COALESCE(t3.store_name,'总部')  else '' end as store_name,\n" +
            "\tcase when \"city_name\" in (${agg_list}) then COALESCE(t3.city_name,'总部')  else '' end as city_name,\n" +
            "\tcase when \"settle_type\" in (${agg_list}) then COALESCE(t2.settle_type,'未知')  else '' end as settle_type,\n" +
            "\tcase when \"settle_name\" in (${agg_list}) then COALESCE(t2.settle_name,'未知')  else '' end as settle_name,\n" +
            "\tcase when \"settle_split_name\" in (${agg_list}) then COALESCE(t2.settle_split_name,'未知')  else '' end as settle_split_name,\n" +
            "\tcase when \"pay_cost_name\" in (${agg_list}) then COALESCE(t1.pay_cost_name,'未知')  else '' end as pay_cost_name,\n" +
            "\tcase when \"order_source_name\" in (${agg_list}) then COALESCE(t5.order_source_name,'未知')  else '' end as order_source_name,\n" +
            "\tcase when \"order_type_name\" in (${agg_list}) then COALESCE(t1.order_type_name,'未知')  else '' end as order_type_name,\n" +
            "\tsum(order_cnt) as order_cnt,\n" +
            "\tsum(receivable_amount) as receivable_amount,\n" +
            "\tsum(cost_amount) as cost_amount,\n" +
            "\tsum(received_amount) AS received_amount from table"
            ;

    public static void main(String[] args) {
//        System.out.println(sql);
//        \s+(((.*)\s+as\s+((\w+)|(`\w+`))\s*,?)+)
        String regex = "select(.*)from";
        Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
        Matcher matcher = pattern.matcher(sql);
        if(matcher.find()){
            String group = matcher.group(1);
            regex="((.*)\\s+as\\s+((\\w+)|(`\\w+`))\\s*,?)+";
            pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(group);

            while (matcher.find()) {
                System.out.format("column:【%s】 function: 【%s 】 found at [%d to %d].%n",
                        matcher.group(3),matcher.group(2), matcher.start(), matcher.end());
            }

        }

    }

    public static List<String> getSqlFormBrackets(String sql) {
        List<String> result = new ArrayList<>();
        int start = 1;
        int end = 0;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '(') {
                start = start + 1;
            } else if (c == ')') {
                end = end + 1;
            }
            if (start == end) {
                result.add(sql.substring(0, i));
                result.add(sql.substring(i + 1));
                break;
            }
        }
        return result;
    }
}
