import logo from './logo.svg';
import styles from './App.module.css';
import UploadFile from './Uploadhandler/UploadFile';
import Knitform from './KnitHandler/Knitform'

function App() {
	return (
		<div className={styles.App}>
			<UploadFile></UploadFile>
			<Knitform></Knitform>
		</div>
	);
}

export default App;
