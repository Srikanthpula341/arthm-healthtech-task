

// Example:
// U1-20240301T101500-payment-failure-300
// U1-20240301T101700-payment-failure-250
// U1-20240301T101800-payment-success-400
// U1-20240301T102500-payment-failure-100
// U2-20240301T103500-payment-failure-200
// U2-20240301T103700-payment-success-500
// U2-20240301T104500-payment-failure-100

// Task:
// Identify users where:

// At least 2 payment failures occur within a 5-minute window
// AND total failed payment amount in that window > 500

// Output:
// Fraud -> [U1]

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record Payment(String user, LocalDateTime time, String status, int amount){};
public class Main {

    public static void main(String[] args) {
        List<String> data = List.of(
                "U1-20240301T101500-payment-failure-300",
                "U1-20240301T101700-payment-failure-250",
                "U1-20240301T101800-payment-success-400",
                "U1-20240301T102500-payment-failure-100",
                "U2-20240301T103500-payment-failure-200",
                "U2-20240301T103700-payment-success-500",
                "U2-20240301T104500-payment-failure-100"
                //0-1-3-4
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        List<Payment> payments = data.stream()
                .map(l -> {
                    String [] p = l.split("-");

                    return new Payment(p[0],LocalDateTime.parse(p[1],formatter),p[3],Integer.parseInt(p[4]));

                }).toList();


        Map<String, List<Payment>> grouping =
                payments.stream()
                        .filter(p->p.status().equals("failure"))
                        .collect(Collectors.groupingBy(Payment::user));

        List<String> fraud = new ArrayList<>();

        for( Map.Entry<String, List<Payment>> entry : grouping.entrySet()){

            //user, []
            String user = entry.getKey();

            List<Payment> list = entry.getValue().stream()
                    .sorted(Comparator.comparing(Payment::time))
                    .toList();
            // 0,1,2,3,4,5

            //System.out.println(list);
            for(int i=0;i<list.size();i++){
                int count =1;
                int sum = list.get(i).amount();

                for(int j=i+1;j<list.size();j++){
                    long diff = Duration.between(list.get(i).time(),list.get(j).time())
                            .toMinutes();

                    if(diff <= 5){
                        count++;
                        sum += list.get(j).amount();
                    }else{
                        break;
                    }
                }

                if(count >=2 && sum > 500){
                    fraud.add(user);
                    break;
                }
            }




        }
        System.out.println(fraud);


    }
}
