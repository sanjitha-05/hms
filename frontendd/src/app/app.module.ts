import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; 
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { PatientComponent } from './patient/patient.component';
import { BookAppointmentComponent } from './book-appointment/book-appointment.component';
import { MedicalHistoryComponent } from './medical-history/medical-history.component';
import { DoctorComponent } from './doctor/doctor.component';
import { PrescriptionComponent } from './prescription/prescription.component';
import { NotificationComponent } from './notification/notifications.component';
import { CtaSectionComponent } from './homefeatures/cta-section/cta-section.component';
import { FeaturesSectionComponent } from './homefeatures/features-section/features-section.component';
import { HeroSectionComponent } from './homefeatures/hero-section/hero-section.component';
import { MyAppointmentsComponent } from './my-appointments/my-appointments.component';
import { ProfileComponent } from './profile/profile.component';
import { DoctorAppointmentsComponent } from './doctor-appointments/doctor-appointments.component';
import { PrescriptionDialogComponent } from './prescription-dialog/prescription-dialog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // Required for Toastr
import { ToastrModule } from 'ngx-toastr';
import { ManageAvailabilityComponent } from './manage-availability/manage-availability.component'; // Import ToastrModule


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    PatientComponent,
    BookAppointmentComponent,
    MedicalHistoryComponent,
    DoctorComponent,
    PrescriptionComponent,
    NotificationComponent,
    CtaSectionComponent,
    FeaturesSectionComponent,
    HeroSectionComponent,
    MyAppointmentsComponent,
    ProfileComponent,
    DoctorAppointmentsComponent,
    PrescriptionDialogComponent,
    ManageAvailabilityComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule, 
    ToastrModule.forRoot({ 
      timeOut: 3000, 
      positionClass: 'toast-top-right',
      preventDuplicates: true, 
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
