import React from 'react';
import styles from './Knitform.module.css';
import axios from 'axios';
import { backendBaseUrl } from '../config';

class Knitform extends React.Component{
	constructor(props){
		super(props);
		this.state = {msg: ''};
	}

	Knit = () => {
		this.setState({msg: 'Knit'})

		let data = new FormData();
		data.append('action', this.state.msg)
		axios.post(`${backendBaseUrl}/knit`, data).then(res => {
			console.log(res.data)
		}).catch(err => this.setState({error: err}));
	}

	render() {
		return (
			<div className={styles.main}>
				<h1 className={styles.header}>Process Uploaded Files</h1>
				<div className={styles.note}>Please note that processing may take up to 30 seconds.</div>
				<div className={styles.buttonContainer}>
					<button className={styles.button} onClick={() => this.Knit()}>Consolidate</button>
					<a className={styles.button} href={backendBaseUrl + "/Processed.xlsx"}>Download</a>
				</div>
			</div>
		)
	}
}

export default Knitform;
