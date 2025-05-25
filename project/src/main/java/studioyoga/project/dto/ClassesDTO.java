package studioyoga.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import studioyoga.project.model.Classes;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassesDTO {
    private Classes classes;
    private int spotsLeft;

}