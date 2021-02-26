import React from 'react';
import styles from './Knitform.module.css';
import axios from 'axios';
import ProgressBar from '../ProgressBar/ProgressBar';
import { backendBaseUrl } from '../config';

class Knitform extends React.Component{
	constructor(props){
		super(props);
		this.state = {
			msg: '',
			showbar: false,
			status: '',
		};
	}

	Knit = () => {
		let data = new FormData();
		data.append('action', 'Knit')
		axios.post(`${backendBaseUrl}/knit`, data).then(res => {
			console.log(res.data)
		}).catch(err => this.setState({error: err}));
	}

	render() {
		const {showbar} = this.state;
		return (
			<div className={styles.main}>
				<h1 className={styles.header}>Process Uploaded Files</h1>
				<div className={styles.note}>Please note that processing may take up to 30 seconds.</div>
				<div className={styles.buttonContainer}>
					<button className={styles.button} onClick={() => {
						this.setState({
							showbar: true,
							msg : "Knit"
						})
						this.Knit()
						console.log(showbar)
					}}>Consolidate</button>
					<a className={styles.button} href={backendBaseUrl + "/Processed.xlsx"}>Download</a>
				</div>
				{showbar ?
				 <ProgressBar status={this.state.status}></ProgressBar>
				 : null}
			</div>


		)
	}
}

export default Knitform;
