import React from 'react';
import './UploadFile.css';
import axios from 'axios';

class UploadFile extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = {file: '', msg: ''};
	}
	
	onFileChange = (event) => {
		this.setState({
			file: event.target.files[0]
		});
	}
	
	uploadFileData = (event) => {
		event.preventDefault();
		this.setState({msg: ''});

		let data = new FormData();
        data.append('file', this.state.file);
        
        axios.post(`http://localhost:8080/upload`,data)
        .then(res => {
            console.log("Hello");
            console.log(res.data);
            this.setState({
                msg:"File Successfully Uploaded"
            })
        }).catch(error => {
            this.setState({
                error : error
            });
        })


	}
	
	render() {
		return (
			<div id="container">
				<h1>Excel Files to Upload</h1>
				<h3>Upload a File</h3>
				<h4>{this.state.msg}</h4>
				<input onChange={this.onFileChange} type="file"></input>
				<button disabled={!this.state.file} onClick={this.uploadFileData}>Upload</button>
			</div>
		)
	}

}

export default UploadFile;