import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { FileuploadService } from '../services/fileupload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent implements OnInit{
  //imageData = "";
  dataUri: string | null = null;
  form!: FormGroup;
  blob!: Blob;

  constructor(private router: Router, private fb: FormBuilder, private fileUploadSvc:FileuploadService){

  }

  ngOnInit(): void {
    this.createForm();
    
  }

  // onFileSelected(event: Event) {
  //   const input = event.target as HTMLInputElement;
  //   if (input.files && input.files.length > 0) {
  //     const file = input.files[0];
  //     console.log(file)
  //     this.imageData = URL.createObjectURL(file);
  //     console.log('Blob URL:', this.imageData);
  //     this.blob = this.dataURItoBlob(this.imageData);
  //   }
  // }

  dataURItoBlob(dataURI: string): Blob {
    const [meta, base64Data] = dataURI.split(',');
    const mimeMatch = meta.match(/:(.*?);/);
    const mimeType = mimeMatch ? mimeMatch[1] : 'application/octet-stream';
  
    const byteString = atob(base64Data);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const intArray = new Uint8Array(arrayBuffer);

    for (let i = 0; i < byteString.length; i++) {
      intArray[i] = byteString.charCodeAt(i);
    }
  
    return new Blob([intArray], { type: mimeType });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      
      // Use FileReader to convert file -> base64 Data URI
      const reader = new FileReader();
      reader.onload = () => {
        this.dataUri = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  upload(){
    if (!this.dataUri) {
      return;
    }

    // Convert the base64 Data URI to a Blob
    this.blob = this.dataURItoBlob(this.dataUri);
    console.log(this.blob);
    const formVal = this.form.value;
    this.fileUploadSvc.upload(formVal, this.blob)
      .then((result)=>{
        this.router.navigate(['/']);
      }).catch(error=> console.log(error))
  }

  private createForm(){
    this.form = this.fb.group({
      comments: this.fb.control<string>(''),
    });
  }

  // dataURItoBlob(dataURI: String){
  //   var byteString = atob(dataURI.split(',')[1]);
  //   let mimeString = dataURI.split(',')[0].split(';')[0];
  //   var ar = new ArrayBuffer(byteString.length);
  //   var ai = new Uint8Array(ar);
  //   for (var i=0; i <byteString.length; i++){
  //     ai[i] = byteString.charCodeAt(i);
  //   }
  //   return new Blob([ar], {type: mimeString});
  // }


}
