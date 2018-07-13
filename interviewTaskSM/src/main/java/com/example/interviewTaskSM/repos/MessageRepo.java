package com.example.interviewTaskSM.repos;

        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;
        import com.example.interviewTaskSM.domain.Message;

        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public interface MessageRepo extends CrudRepository<Message, Long> {
}