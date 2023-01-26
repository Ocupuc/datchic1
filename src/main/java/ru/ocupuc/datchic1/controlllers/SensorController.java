package ru.ocupuc.datchic1.controlllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ocupuc.datchic1.dto.SensorDTO;
import ru.ocupuc.datchic1.models.Sensor;
import ru.ocupuc.datchic1.services.SensorService;
import ru.ocupuc.datchic1.util.MeasurementErrorResponse;
import ru.ocupuc.datchic1.util.MeasurementException;
import ru.ocupuc.datchic1.util.SensorValidator;

import javax.validation.Valid;

import static ru.ocupuc.datchic1.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult){
        Sensor sensorToAdd = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToAdd, bindingResult);

        if(bindingResult.hasErrors()) returnErrorsToClient(bindingResult);

        sensorService.register(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
