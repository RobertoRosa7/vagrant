package service.reading.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaService;
import common.kafka.Message;

public class ReadingReportService {
  private final String topic = "ECOMMERCE_USER_GENERATE_READING_REPORT";
  private final Path SOURCE = new File("src/main/resources/report.txt").toPath();

  public static void main(String[] args) throws InterruptedException, ExecutionException{
    var reportService = new ReadingReportService();

    try (var service = new KafkaService<>(
        ReadingReportService.class.getSimpleName(),
        reportService.topic,
        reportService::parser,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Message<User>> record) throws IOException {
    var message = record.value();
    System.out.println("-----------------------------------------");
    System.out.println("Processing new report for " + record.value());

    var user = message.getPayload();
    var target = new File(user.getReportPath());
    IO.copyTo(SOURCE, target);
    IO.append(target, "Created for " + user.getUserUuid());

    System.out.println("File created: " + target.getAbsolutePath());
  }
}
