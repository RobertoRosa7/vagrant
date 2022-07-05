package service.reading.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaDispatcher;
import common.kafka.KafkaService;

public class ReadingReportService {
  private final String topic = "USER_GENERATE_READING_REPORT";
  private final KafkaDispatcher<User> dispatcher = new KafkaDispatcher<>();
  private final Path SOURCE = new File("src/main/resources/report.txt").toPath();

  public static void main(String[] args) {
    var reportService = new ReadingReportService();

    try (var service = new KafkaService<>(
        ReadingReportService.class.getSimpleName(),
        reportService.topic,
        reportService::parser,
        User.class,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, User> record) throws IOException {
    System.out.println("-----------------------------------------");
    System.out.println("Processing new order, checking for reportService");
    System.out.println("Value => " + record.value());

    var user = record.value();
    var target = new File(user.getReportPath());
    IO.copyTo(SOURCE, target);
    IO.append(target, "Created for " + user.getUserUuid());

    System.out.println("File created: " + target.getAbsolutePath());
  }
}
