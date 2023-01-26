package ru.ocupuc.datchic1.controlllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ocupuc.datchic1.dto.MeasurementDTO;
import ru.ocupuc.datchic1.dto.MeasurementsResponse;
import ru.ocupuc.datchic1.models.Measurement;
import ru.ocupuc.datchic1.services.MeasurementService;
import ru.ocupuc.datchic1.util.MeasurementErrorResponse;
import ru.ocupuc.datchic1.util.MeasurementException;
import ru.ocupuc.datchic1.util.MeasurementValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static ru.ocupuc.datchic1.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    public MeasurementsController(MeasurementService measurementService,
                                  MeasurementValidator measurementValidator,
                                  ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult){
        Measurement measuremenToAdd = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measuremenToAdd, bindingResult);
        if (bindingResult.hasErrors())returnErrorsToClient(bindingResult);
        measurementService.addMeasurement(measuremenToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public MeasurementsResponse getMeasurement(){
        return new MeasurementsResponse(measurementService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount(){
        return measurementService.findAll().stream().filter(Measurement:: isRaining).count();
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
