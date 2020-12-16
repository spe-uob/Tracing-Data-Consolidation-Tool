import React from 'react';
import styles from './UploadFile.module.css';

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

		fetch('http://localhost:8080/upload', {
			method: 'POST',
			body: data
		}).then(response => {
			this.setState({msg: "File successfully uploaded"});
		}).catch(err => {
			this.setState({error: err});
		});

	}

	render() {
		return (
			<div className={styles.main}>
				<h1 className={styles.header}>Excel Files to Upload</h1>
				<h3 className={styles.header}>Upload a File</h3>
				<input onChange={this.onFileChange} type="file" />
				<button className={styles.button} disabled={!this.state.file} onClick={this.uploadFileData}>Upload</button>
				<h4 className={styles.statusMessage}>{this.state.msg}</h4>
			</div>
		)
	}
}

export default UploadFile;
