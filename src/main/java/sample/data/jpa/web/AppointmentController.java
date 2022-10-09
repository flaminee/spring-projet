package sample.data.jpa.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.AppException;
import sample.data.jpa.Error;
import sample.data.jpa.dao.AppointmentDao;
import sample.data.jpa.domain.Appointment;
import sample.data.jpa.domain.Client;
import sample.data.jpa.domain.Prof;
import sample.data.jpa.domain.Role;
import sample.data.jpa.dto.AppointmentDto;
import sample.data.jpa.dto.UserDto;
import javax.annotation.security.RolesAllowed;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentDao appointmentDao;

    @RolesAllowed(Role.PROF)
    @PostMapping("/create")
    @ResponseBody
    public Appointment create(@RequestBody AppointmentDto.Create create, @AuthenticationPrincipal UserDto.Auth authUser) {
        Prof prof = new Prof(authUser.getId(), authUser.getEmail(), authUser.getName());
        Appointment appointment = new Appointment(
                Timestamp.valueOf(create.getStart()),
                Timestamp.valueOf(create.getEnd()),
                create.getTitle()
        );
        appointment.setProf(prof);
        appointmentDao.save(appointment);
        return appointment;
    }

    @RolesAllowed(Role.PROF)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        Appointment appointment = appointmentDao.findById(id).orElseThrow(() -> new AppException(Error.APPOINTMENT_NOT_FOUND));
        if(appointment.getProf().getEmail() != authUser.getEmail()){throw new AppException(Error.UNAUTHORIZED_SUPPRESSION);}
        appointmentDao.delete(appointment);
        return "successful deletion";
    };

    @RolesAllowed(Role.CLIENT)
    @PutMapping("/register/{id}")
    @ResponseBody
    public Appointment register(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        Appointment appointment = appointmentDao.findById(id).orElseThrow(() -> new AppException(Error.APPOINTMENT_NOT_FOUND));
        if(appointment.getClient() != null ){throw new AppException(Error.ALREADY_BOOKED);}
        Client client = new Client(authUser.getId(), authUser.getEmail(), authUser.getName());
        appointment.setClient(client);
        appointmentDao.flush();
        return appointment;
    }

    @RolesAllowed(Role.CLIENT)
    @PutMapping("/unregister/{id}")
    @ResponseBody
    public Appointment unregister(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {

        Appointment appointment = appointmentDao.findById(id).orElseThrow(() -> new AppException(Error.APPOINTMENT_NOT_FOUND));
        if(appointment.getClient() == null ){throw new AppException(Error.ALREADY_UNREGISTER);}
        if(appointment.getClient().getEmail() != authUser.getEmail()){throw new AppException(Error.UNAUTHORIZED_UNREGISTER);}
        appointment.setClient(null);
        appointmentDao.flush();
        return appointment;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Appointment> getAppointment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        Optional<Appointment> appointment = appointmentDao.findById(id);
        if( appointment.isPresent()){
            return ResponseEntity.ok().body(appointment.get());
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/byIdProf/{id}")
    @ResponseBody
    public List<Appointment> byIdProf(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        return appointmentDao.byIdProf(id);
    }

    @GetMapping("/byIdClient/{id}")
    @ResponseBody
    public List<Appointment> byIdClient(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        return appointmentDao.byIdClient(id);
    }

    @GetMapping("/freeByIdProf/{id}")
    @ResponseBody
    public List<Appointment> freeByIdProf(@PathVariable("id") Long id, @AuthenticationPrincipal UserDto.Auth authUser) {
        return appointmentDao.freeByIdProf(id);
    }

}