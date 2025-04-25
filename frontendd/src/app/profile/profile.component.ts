import { Component, OnInit } from '@angular/core';
import { PatientService } from '../services/patient.service';
import axios from 'axios'; // Import axios for API calls
import { ToastrService } from 'ngx-toastr'; // Import ToastrService if you are using it

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  patientDetails: any = {};
  errorMessage: string = '';
  email: string = ''; 
  password: string = ''; 

  isEditMode: boolean = false;
  updatedName: string = '';
  updatedContactDetails: string = '';
  updatedDateOfBirth: string = '';

  showUpdatePasswordDialog: boolean = false;
  currentPassword: string = '';
  newPassword: string = '';

  constructor(private patientService: PatientService, private toastr: ToastrService) {} 

  ngOnInit(): void {
    this.loadPatientDetails();
  }

  async loadPatientDetails() {
    const storedPatientId = localStorage.getItem('userId');
    if (storedPatientId) {
      const patientId = parseInt(storedPatientId, 10);
      await this.fetchPatientDetails(patientId);
    } else {
      console.error('No patient ID found in localStorage.');
      this.errorMessage = 'Failed to load patient details. Please log in again.';
    }
  }

  async fetchPatientDetails(patientId: number) {
    try {
      const data = await this.patientService.getPatientDetails(patientId);
      this.patientDetails = data;
      this.email = data.email;
      this.password = data.password;
      this.updatedName = data.name;
      this.updatedContactDetails = data.contactDetails;
      this.updatedDateOfBirth = this.formatDate(data.dateOfBirth); 
    } catch (error) {
      console.error('Error fetching patient details:', error);
      this.errorMessage = 'Failed to load patient details. Please try again later.';
    }
  }

  enableEditMode() {
    this.isEditMode = true;
  }

  cancelEditMode() {
    this.isEditMode = false;
    this.updatedName = this.patientDetails.name;
    this.updatedContactDetails = this.patientDetails.contactDetails;
    this.updatedDateOfBirth = this.formatDate(this.patientDetails.dateOfBirth);
  }

  async updateProfile() {
    const storedPatientId = localStorage.getItem('userId');
    if (!storedPatientId) {
      this.toastr.error('User ID not found. Please log in again.', 'Error');
      return;
    }
    const patientId = parseInt(storedPatientId, 10);
    const updateData = {
      patientId: patientId,
      name: this.updatedName,
      contactDetails: this.updatedContactDetails,
      dateOfBirth: this.updatedDateOfBirth
    };

    try {
      const token = localStorage.getItem('token');
      const response = await axios.put(`http://localhost:8080/api/hospital/patients/put/${patientId}`, updateData, { // Use the existing PUT endpoint
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.status === 200) {
        this.toastr.success('Profile updated successfully!', 'Success');
        this.isEditMode = false;
        await this.loadPatientDetails(); 
      } else {
        this.toastr.error('Failed to update profile.', 'Error');
      }
    } catch (error: any) {
      console.error('Error updating profile:', error);
      this.toastr.error('Failed to update profile. Please try again later.', 'Error');
    }
  }

  openUpdatePasswordDialog() {
    this.showUpdatePasswordDialog = true;
    this.currentPassword = '';
    this.newPassword = '';
  }

  closeUpdatePasswordDialog() {
    this.showUpdatePasswordDialog = false;
  }

  async updatePassword() {
    const storedPatientId = localStorage.getItem('userId');
    if (!storedPatientId) {
        this.toastr.error('User ID not found. Please log in again.', 'Error');
        return;
    }
    const userId = parseInt(storedPatientId, 10);

    const passwordUpdateData = {
        currentPassword: this.currentPassword,
        newPassword: this.newPassword,
    };

    try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`http://localhost:8080/api/users/${userId}/password`, passwordUpdateData, { 
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.status === 200) {
            this.toastr.success('Password updated successfully!', 'Success');
            this.closeUpdatePasswordDialog();
            this.currentPassword = '';
            this.newPassword = '';
        } else if (response.status === 401) {
            this.toastr.error('Incorrect current password.', 'Error');
        } else {
            this.toastr.error('Failed to update password.', 'Error');
        }
    } catch (error: any) {
        console.error('Error updating password:', error);
        this.toastr.error('Failed to update password. Please try again later.', 'Error');
    }
}

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`; 
  }
}